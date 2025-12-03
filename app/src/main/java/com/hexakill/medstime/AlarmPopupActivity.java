package com.hexakill.medstime;

import android.app.Activity;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexakill.medstime.database.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmPopupActivity extends Activity {

    private MyDbHelper dbHelper;
    private LinearLayout medicineListLayout;
    private TextView alarmTimeText;
    private Button dismissButton;
    private Ringtone ringtone;

    private static final long ALARM_WINDOW_MS = 60_000; // 1 minute fallback window

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_popup);

        // Show on lock screen and fullscreen
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
        dismissButton.setOnClickListener(v -> {
            stopRingtone();
            finish();
        });

        // Play ringtone
        String ringtoneUriStr = getIntent().getStringExtra("ringtoneUri");
        try {
            Uri ringtoneUri = ringtoneUriStr != null ? Uri.parse(ringtoneUriStr) :
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            if (ringtone != null) ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display current time
        alarmTimeText.setText(android.text.format.DateFormat.format("hh:mm a", System.currentTimeMillis()));

        // Get all alarm IDs from intent
        int[] alarmIds = getIntent().getIntArrayExtra("alarm_ids");
        if (alarmIds != null && alarmIds.length > 0) {
            showAlarmsByIds(alarmIds);
        } else {
            showActiveAlarmsFallback();
        }
    }

    private void showAlarmsByIds(int[] alarmIds) {
        medicineListLayout.removeAllViews();
        List<AlarmSet> alarms = new ArrayList<>();
        alarms.addAll(dbHelper.getAllUserReminders());
        alarms.addAll(dbHelper.getAllPremadeReminders());

        boolean hasMedicine = false;

        for (AlarmSet alarm : alarms) {
            if (!alarm.isActive()) continue;
            for (int id : alarmIds) {
                if (alarm.getId() == id) {
                    displayMedicine(alarm);
                    hasMedicine = true;
                    break;
                }
            }
        }

        if (!hasMedicine) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No medicines scheduled.");
            emptyView.setTextColor(Color.WHITE);
            emptyView.setTextSize(18f);
            medicineListLayout.addView(emptyView);
        }
    }

    private void showActiveAlarmsFallback() {
        medicineListLayout.removeAllViews();
        List<AlarmSet> alarms = new ArrayList<>();
        alarms.addAll(dbHelper.getAllUserReminders());
        alarms.addAll(dbHelper.getAllPremadeReminders());

        long now = System.currentTimeMillis();
        boolean hasMedicine = false;

        for (AlarmSet alarm : alarms) {
            if (!alarm.isActive()) continue;
            if (now >= alarm.getStartTime() && now < alarm.getStartTime() + ALARM_WINDOW_MS) {
                displayMedicine(alarm);
                hasMedicine = true;
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

    private void displayMedicine(AlarmSet alarm) {
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
        medName.setText(alarm.getMedicineName());
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

    private void stopRingtone() {
        if (ringtone != null && ringtone.isPlaying()) ringtone.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRingtone();
    }
}
