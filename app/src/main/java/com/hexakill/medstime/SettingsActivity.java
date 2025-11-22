package com.hexakill.medstime;

import android.app.Activity;
import android.content.Intent;
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

    // Snooze interval options
    private final int[] snoozeOptions = {1, 2, 3, 5, 10, 15, 30};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        HeaderManager.setupHeader(this);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Ringtone button
        btnSelectRingtone = findViewById(R.id.btnRingtone);
        btnSelectRingtone.setOnClickListener(v -> openRingtonePicker());

        // Snooze seekbar and label
        seekBarSnooze = findViewById(R.id.seekBarSnooze);
        snoozeValueLabel = findViewById(R.id.snoozeLabel);

        seekBarSnooze.setMax(snoozeOptions.length - 1);
        seekBarSnooze.setProgress(0);
        updateSnoozeLabel(0);

        seekBarSnooze.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSnoozeLabel(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Notification before ringing
        switchNotification = findViewById(R.id.switchNotification);
        switchNotification.setChecked(true);

        // Permission toggle
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
            if (selectedRingtoneUri == null) {
                Toast.makeText(this, "Please select a ringtone first", Toast.LENGTH_SHORT).show();
                return;
            }
            // Launch AlarmActivity for testing
            Intent intent = new Intent(this, AlarmActivity.class);
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
        if (requestCode == REQUEST_CODE_RINGTONE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (selectedRingtoneUri != null) {
                    btnSelectRingtone.setText("Ringtone Selected");
                } else {
                    btnSelectRingtone.setText("Select Ringtone");
                }
            }
        }
    }
}
