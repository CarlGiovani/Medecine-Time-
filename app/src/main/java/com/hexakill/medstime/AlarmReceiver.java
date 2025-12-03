package com.hexakill.medstime;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hexakill.medstime.database.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "meds_alarm_channel";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {

        String ringtoneUri = intent.getStringExtra("ringtoneUri");
        MyDbHelper dbHelper = new MyDbHelper(context);
        List<Integer> activeAlarmIds = new ArrayList<>();
        long now = System.currentTimeMillis();

        // Collect alarms due now
        for (AlarmSet alarm : dbHelper.getAllUserReminders()) {
            if (alarm.isActive() && isAlarmDue(alarm, now)) activeAlarmIds.add(alarm.getId());
        }
        for (AlarmSet alarm : dbHelper.getAllPremadeReminders()) {
            if (alarm.isActive() && isAlarmDue(alarm, now)) activeAlarmIds.add(alarm.getId());
        }

        if (activeAlarmIds.isEmpty()) return;

        // ---------------- DIRECT POPUP LAUNCH (REQUIRED) ----------------
        Intent popupIntent = new Intent(context, AlarmPopupActivity.class);
        popupIntent.putExtra("alarm_ids", activeAlarmIds.stream().mapToInt(i -> i).toArray());
        popupIntent.putExtra("ringtoneUri", ringtoneUri);
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(popupIntent);   // <-- YOU REMOVED THIS. IT MUST BE HERE.


        // ---------------- FULL SCREEN NOTIFICATION (BACKUP) ----------------
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(),
                popupIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Channel creation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Alarm",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Alarms that need full screen popup");
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);

            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmSound == null)
            alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Time to take your medicine")
                .setContentText("Tap to view your medicines")
                .setSmallIcon(R.drawable.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat.from(context)
                .notify((int) System.currentTimeMillis(), builder.build());
    }

    private boolean isAlarmDue(AlarmSet alarm, long now) {
        long start = alarm.getStartTime();
        int intervalHours = Integer.parseInt(alarm.getAlarmInterval());
        long intervalMillis = intervalHours * 60L * 60L * 1000L;

        long next = start;
        while (next < now) next += intervalMillis;

        return now >= (next - intervalMillis) && now < next;
    }
}
