package com.hexakill.medstime;

import android.content.Context;
import android.os.PowerManager;

public class BatteryUtils {

    public static boolean isBatteryOptimized(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isIgnoringBatteryOptimizations(context.getPackageName()) == false;
    }
}
