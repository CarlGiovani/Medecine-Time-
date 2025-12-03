package com.hexakill.medstime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RINGTONE = 1001;

    private Uri selectedRingtoneUri;
    private String selectedRingtoneName;

    private Button btnSelectRingtone, btnTestAlarm;
    private Switch switchPermission, switchFullScreen, switchBattery;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        HeaderManager.setupHeader(this);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        btnSelectRingtone = findViewById(R.id.btnRingtone);
        btnTestAlarm = findViewById(R.id.btnTestAlarm);
        switchPermission = findViewById(R.id.switchPermission);
        switchFullScreen = findViewById(R.id.switchFullScreenPermission);
        switchBattery = findViewById(R.id.switchBatteryOptimization);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        loadSavedRingtone();
        setupOverlayPermissionSwitch();
        setupFullScreenSwitch();
        setupBatteryOptimizationSwitch();
        setupRingtonePicker();
        setupTestAlarmButton();
    }

    // ---------------- Load saved ringtone ----------------
    private void loadSavedRingtone() {
        String savedRingtone = prefs.getString("alarm_ringtone", null);
        String savedRingtoneName = prefs.getString("alarm_ringtone_name", null);

        if (savedRingtone != null) {
            selectedRingtoneUri = Uri.parse(savedRingtone);
            selectedRingtoneName = savedRingtoneName != null ? savedRingtoneName : "Selected Ringtone";
            btnSelectRingtone.setText(selectedRingtoneName);
        }
    }

    // ---------------- Overlay permission (Draw over apps) ----------------
    private void setupOverlayPermissionSwitch() {
        switchPermission.setChecked(false); // default unchecked
        switchPermission.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivity(intent);
        });
    }

    // ---------------- Full-screen notification permission ----------------
    private void setupFullScreenSwitch() {
        // The switch is just a trigger, so set initial state to false or read your own preference
        switchFullScreen.setChecked(false);

        switchFullScreen.setOnClickListener(v -> {
            // Open the app-specific settings page
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        });
    }



    // ---------------- Battery optimization ----------------
    private void setupBatteryOptimizationSwitch() {
        switchBattery.setChecked(false); // default unchecked
        switchBattery.setOnClickListener(v -> {
            if (isBatteryOptimized()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        .setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
    }

    private boolean isBatteryOptimized() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return pm != null && !pm.isIgnoringBatteryOptimizations(getPackageName());
    }

    // ---------------- Ringtone picker ----------------
    private void setupRingtonePicker() {
        btnSelectRingtone.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Ringtone");
            startActivityForResult(intent, REQUEST_CODE_RINGTONE);
        });
    }

    // ---------------- Test alarm ----------------
    private void setupTestAlarmButton() {
        btnTestAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(this, AlarmPopupActivity.class);
            intent.putExtra("ringtoneUri",
                    selectedRingtoneUri != null ? selectedRingtoneUri.toString() : null);
            startActivity(intent);
        });
    }

    // ---------------- Handle ringtone result ----------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RINGTONE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            selectedRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (selectedRingtoneUri != null) {
                String ringtoneName = RingtoneManager.getRingtone(this, selectedRingtoneUri)
                        .getTitle(this);

                selectedRingtoneName = ringtoneName;
                btnSelectRingtone.setText(ringtoneName);

                prefs.edit()
                        .putString("alarm_ringtone", selectedRingtoneUri.toString())
                        .putString("alarm_ringtone_name", ringtoneName)
                        .apply();

            } else {
                selectedRingtoneName = null;
                btnSelectRingtone.setText("Select Ringtone");

                prefs.edit()
                        .remove("alarm_ringtone")
                        .remove("alarm_ringtone_name")
                        .apply();
            }
        }
    }

    // ---------------- Update switches on resume ----------------
    @Override
    protected void onResume() {
        super.onResume();
        switchPermission.setChecked(Settings.canDrawOverlays(this));
        switchFullScreen.setChecked(NotificationUtils.isFullScreenEnabled(this));
        switchBattery.setChecked(!isBatteryOptimized());
    }

}
