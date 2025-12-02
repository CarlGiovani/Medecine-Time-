package com.hexakill.medstime;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.Calendar;

public class AlarmAddUserActivity extends AppCompatActivity {

    private EditText medicineNameInput, noteInput;
    private SeekBar everyXSeekBar;
    private TextView everyXSeekBarLabel;
    private TimePicker timePicker;

    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_user);

        HeaderManager.setupHeader(this);

        dbHelper = new MyDbHelper(this);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        medicineNameInput = findViewById(R.id.medicineNameInput);
        noteInput = findViewById(R.id.noteInput);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
        timePicker = findViewById(R.id.timePicker);

        timePicker.setIs24HourView(false);

        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                everyXSeekBarLabel.setText("Interval (hours): " + (progress + 1));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.saveAlarmFab).setOnClickListener(v -> saveAlarm());
    }

    private void saveAlarm() {
        String name = medicineNameInput.getText().toString().trim();
        String note = noteInput.getText().toString().trim();

        if (name.isEmpty()) {
            medicineNameInput.setError("Name is required");
            return;
        }

        int intervalHours = everyXSeekBar.getProgress() + 1;

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

        dbHelper.addUserReminder(
                name,
                "",                       // user alarms have no description
                String.valueOf(intervalHours),
                note,
                startTimeMillis
        );

        AlarmSet alarm = new AlarmSet(0, name, String.valueOf(intervalHours), note, startTimeMillis);
        alarm.computeNextAlarmTime();

        scheduleAlarm(alarm);

        Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlarmAddUserActivity.this, HomepageActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAlarm(AlarmSet alarm) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medicine_name", alarm.getMedicineName());
        intent.putExtra("alarm_note", alarm.getAlarmNote());
        intent.putExtra("alarm_time", alarm.getNextAlarmTime());

        long intervalMillis = Integer.parseInt(alarm.getAlarmInterval()) * 3600_000L;
        intent.putExtra("alarm_interval", intervalMillis);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarm.getNextAlarmTime(),
                    pendingIntent
            );
        }
    }
}
