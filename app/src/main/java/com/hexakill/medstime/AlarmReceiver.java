package com.hexakill.medstime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract medicine info and note
        String medicineName = intent.getStringExtra("medicine_name");
        String alarmNote = intent.getStringExtra("alarm_note");
        long alarmTimeMillis = intent.getLongExtra("alarm_time", System.currentTimeMillis());

        // Optional: show a short Toast for debug
        Toast.makeText(context, "Time to take: " + medicineName, Toast.LENGTH_SHORT).show();

        // Start the AlarmPopupActivity in a new task
        Intent popupIntent = new Intent(context, AlarmPopupActivity.class);
        popupIntent.putExtra(AlarmPopupActivity.EXTRA_ALARM_TIME, alarmTimeMillis);
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        context.startActivity(popupIntent);

        // Optional: reschedule next alarm if interval is provided
        long intervalMillis = intent.getLongExtra("alarm_interval", 0);
        if (intervalMillis > 0) {
            scheduleNextAlarm(context, medicineName, alarmNote, alarmTimeMillis + intervalMillis, intervalMillis);
        }
    }

    private void scheduleNextAlarm(Context context, String medicineName, String alarmNote, long triggerAtMillis, long intervalMillis) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("medicine_name", medicineName);
        intent.putExtra("alarm_note", alarmNote);
        intent.putExtra("alarm_time", triggerAtMillis);
        intent.putExtra("alarm_interval", intervalMillis);

        int requestCode = (int) System.currentTimeMillis(); // unique ID
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(
                context, requestCode, intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );

        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }
}
