package com.hexakill.medstime;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtils {

    public static boolean isFullScreenEnabled(Context context) {
        if (Build.VERSION.SDK_INT < 29) return true;

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return nm.areNotificationsEnabled();
    }
}
