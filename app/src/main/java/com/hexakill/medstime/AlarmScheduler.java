package com.hexakill.medstime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmScheduler {

    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleAlarm(Context context, AlarmSet alarm, String ringtoneUri) {
        if (!alarm.isActive()) return;

        // Interval in milliseconds
        long intervalMillis = Long.parseLong(alarm.getAlarmInterval()) * 3600_000L;

        // Prepare intent for receiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("alarm_id", alarm.getId());
        intent.putExtra("ringtoneUri", ringtoneUri);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (am != null) {
            long nextTime = alarm.getNextAlarmTime();

            // Use exact alarms for both user and preset alarms
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, nextTime, pendingIntent);
            }

        }
    }

    public static void cancelAlarm(Context context, AlarmSet alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                alarm.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(pendingIntent);
        }
    }
}
