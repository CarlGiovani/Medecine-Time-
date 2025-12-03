package com.hexakill.medstime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.Calendar;

public class AlarmAddPresetActivity extends AppCompatActivity {

    private TextView tvMedicineName, tvMedicineDescription, intervalLabel;
    private EditText noteInput;
    private TimePicker timePicker;
    private MyDbHelper dbHelper;
    private FloatingActionButton saveFab;
    private androidx.appcompat.widget.AppCompatSeekBar intervalSeekBar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_preset);

        HeaderManager.setupHeader(this);

        dbHelper = new MyDbHelper(this);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineDescription = findViewById(R.id.tvMedicineDescription);
        intervalLabel = findViewById(R.id.everyXSeekBarLabel);
        noteInput = findViewById(R.id.noteInput);
        timePicker = findViewById(R.id.timePicker);
        intervalSeekBar = findViewById(R.id.everyXSeekBar);
        saveFab = findViewById(R.id.saveAlarmFab);

        timePicker.setIs24HourView(false);

        String name = getIntent().getStringExtra("medicine_name");
        String desc = getIntent().getStringExtra("medicine_description");

        tvMedicineName.setText(name != null ? name : "");
        tvMedicineDescription.setText(desc != null ? desc : "");

        intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intervalLabel.setText("Interval (hours): " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        saveFab.setOnClickListener(v -> savePresetAlarm());
    }

    private void savePresetAlarm() {
        String name = tvMedicineName.getText().toString();
        String desc = tvMedicineDescription.getText().toString();
        String note = noteInput.getText().toString();
        int intervalHours = intervalSeekBar.getProgress() + 1;

        int hour, minute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        long startTimeMillis = cal.getTimeInMillis();

        // Add to PREBUILT reminders table and get the inserted row ID
        long newId = dbHelper.addPrebuiltReminder(
                name,
                desc,
                intervalHours,
                note,
                startTimeMillis
        );

        // Create AlarmSet for scheduling
        AlarmSet alarm = new AlarmSet((int)newId, name, String.valueOf(intervalHours), note, startTimeMillis);
        alarm.computeNextAlarmTime();
        alarm.setUserCreated(false);

        // Get ringtone from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String ringtoneUri = prefs.getString("alarm_ringtone", null);

        // Schedule the alarm with ringtone
        AlarmScheduler.scheduleAlarm(this, alarm, ringtoneUri);

        Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlarmAddPresetActivity.this, HomepageActivity.class);
        startActivity(intent);
        finish();
    }
}
