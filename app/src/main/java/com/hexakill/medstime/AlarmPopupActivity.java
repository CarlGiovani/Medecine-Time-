package com.hexakill.medstime;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hexakill.medstime.database.MyDbHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlarmPopupActivity extends Activity {

    private MyDbHelper dbHelper;
    private LinearLayout medicineListLayout;
    private TextView alarmTimeText;
    private Button dismissButton;

    public static final String EXTRA_ALARM_TIME = "alarm_time"; // in millis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_popup);

        // Fullscreen, keep screen on, show over lock screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        );

        dbHelper = new MyDbHelper(this);

        medicineListLayout = findViewById(R.id.alarm_medicine_list);
        alarmTimeText = findViewById(R.id.alarm_time);
        dismissButton = findViewById(R.id.alarm_dismiss_button);

        dismissButton.setText("Confirm");
        dismissButton.setOnClickListener(v -> finish());

        long alarmTimeMillis = getIntent().getLongExtra(EXTRA_ALARM_TIME, System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(alarmTimeMillis);
        String formattedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(cal.getTime());
        alarmTimeText.setText(formattedTime);

        loadMedicinesForTime(alarmTimeMillis);
    }

    private void loadMedicinesForTime(long alarmMillis) {
        medicineListLayout.removeAllViews();
        List<AlarmSet> alarms = dbHelper.getAllUserReminders();

        boolean hasMedicine = false;
        Calendar targetCal = Calendar.getInstance();
        targetCal.setTimeInMillis(alarmMillis);

        for (AlarmSet alarm : alarms) {
            if (!alarm.isActive()) continue;

            Calendar alarmCal = Calendar.getInstance();
            alarmCal.setTimeInMillis(alarm.getStartTime());

            if (alarmCal.get(Calendar.HOUR_OF_DAY) == targetCal.get(Calendar.HOUR_OF_DAY) &&
                    alarmCal.get(Calendar.MINUTE) == targetCal.get(Calendar.MINUTE)) {

                hasMedicine = true;

                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.VERTICAL);
                itemLayout.setPadding(20, 20, 20, 20);
                itemLayout.setBackgroundColor(Color.parseColor("#33000000"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 15);
                itemLayout.setLayoutParams(params);

                TextView medName = new TextView(this);
                medName.setText("Medicine: " + alarm.getMedicineName());
                medName.setTextColor(Color.WHITE);
                medName.setTextSize(20f);

                TextView medNote = new TextView(this);
                medNote.setText("Note: " + alarm.getAlarmNote());
                medNote.setTextColor(Color.WHITE);
                medNote.setTextSize(16f);

                itemLayout.addView(medName);
                itemLayout.addView(medNote);

                medicineListLayout.addView(itemLayout);
            }
        }

        if (!hasMedicine) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No medicines scheduled for this time.");
            emptyView.setTextColor(Color.WHITE);
            emptyView.setTextSize(18f);
            medicineListLayout.addView(emptyView);
        }
    }
}
