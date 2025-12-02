package com.hexakill.medstime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RINGTONE = 1001;

    private Uri selectedRingtoneUri;
    private Button btnSelectRingtone, btnTestAlarm;
    private SeekBar seekBarSnooze;
    private TextView snoozeValueLabel;
    private Switch switchNotification, switchPermission;

    private SharedPreferences prefs;

    private final int[] snoozeOptions = {1, 2, 3, 5, 10, 15, 30};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        HeaderManager.setupHeader(this);

        prefs = getSharedPreferences("settings", MODE_PRIVATE);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Ringtone button
        btnSelectRingtone = findViewById(R.id.btnRingtone);
        btnSelectRingtone.setOnClickListener(v -> openRingtonePicker());

        // Load saved ringtone
        String savedRingtone = prefs.getString("alarm_ringtone", null);
        if (savedRingtone != null) {
            selectedRingtoneUri = Uri.parse(savedRingtone);
            btnSelectRingtone.setText("Ringtone Selected");
        }

        // Snooze seekbar and label
        seekBarSnooze = findViewById(R.id.seekBarSnooze);
        snoozeValueLabel = findViewById(R.id.snoozeLabel);

        int savedSnoozeIndex = prefs.getInt("snooze_index", 0);
        seekBarSnooze.setMax(snoozeOptions.length - 1);
        seekBarSnooze.setProgress(savedSnoozeIndex);
        updateSnoozeLabel(savedSnoozeIndex);

        seekBarSnooze.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSnoozeLabel(progress);
                prefs.edit().putInt("snooze_index", progress).apply();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Notification switch
        switchNotification = findViewById(R.id.switchNotification);
        boolean savedNotify = prefs.getBoolean("notify_before_alarm", true);
        switchNotification.setChecked(savedNotify);
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("notify_before_alarm", isChecked).apply()
        );

        // Overlay permission
        switchPermission = findViewById(R.id.switchPermission);
        switchPermission.setChecked(Settings.canDrawOverlays(this));
        switchPermission.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
            }
        });

        // Test alarm button
        btnTestAlarm = findViewById(R.id.btnTestAlarm);
        btnTestAlarm.setOnClickListener(v -> {
/*            if (selectedRingtoneUri == null) {
                Toast.makeText(this, "Please select a ringtone first", Toast.LENGTH_SHORT).show();
                return;
            }*/
            Intent intent = new Intent(this, AlarmPopupActivity.class);
            intent.putExtra("ringtoneUri", selectedRingtoneUri.toString());
            startActivity(intent);
        });
    }

    private void updateSnoozeLabel(int index) {
        int minutes = snoozeOptions[index];
        snoozeValueLabel.setText("Snooze Interval: " + minutes + " min");
    }

    private void openRingtonePicker() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Ringtone");
        startActivityForResult(intent, REQUEST_CODE_RINGTONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RINGTONE && resultCode == Activity.RESULT_OK && data != null) {
            selectedRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (selectedRingtoneUri != null) {
                btnSelectRingtone.setText("Ringtone Selected");
                prefs.edit().putString("alarm_ringtone", selectedRingtoneUri.toString()).apply();
            } else {
                btnSelectRingtone.setText("Select Ringtone");
                prefs.edit().remove("alarm_ringtone").apply();
            }
        }
    }
}
