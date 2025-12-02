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

public class AlarmAddPresetActivity extends AppCompatActivity {

    private TextView tvMedicineName, tvMedicineDescription, intervalLabel;
    private SeekBar intervalSeekBar;
    private EditText noteInput;
    private TimePicker timePicker;

    private MyDbHelper dbHelper;

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
        intervalSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(false);

        String name = getIntent().getStringExtra("medicine_name");
        String desc = getIntent().getStringExtra("medicine_description");

        tvMedicineName.setText(name != null ? name : "");
        tvMedicineDescription.setText(desc != null ? desc : "");

        intervalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intervalLabel.setText("Interval (hours): " + (progress + 1));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        findViewById(R.id.saveAlarmFab).setOnClickListener(v -> savePresetAlarm());
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

        // PREBUILT REMINDERS ALSO GO INTO USER REMINDERS TABLE
        dbHelper.addUserReminder(
                name,
                desc,
                String.valueOf(intervalHours),
                note,
                startTimeMillis
        );

        // For AlarmScheduler
        AlarmSet alarm = new AlarmSet(0, name, String.valueOf(intervalHours), note, startTimeMillis);
        alarm.computeNextAlarmTime();

        scheduleAlarm(alarm);

        Toast.makeText(this, "Alarm Added!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AlarmAddPresetActivity.this, HomepageActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("ScheduleExactAlarm")
    private void scheduleAlarm(AlarmSet alarm) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medicine_name", alarm.getMedicineName());
        intent.putExtra("alarm_note", alarm.getAlarmNote());
        intent.putExtra("alarm_time", alarm.getNextAlarmTime());

        long intervalMillis = Long.parseLong(alarm.getAlarmInterval()) * 3600_000L;
        intent.putExtra("alarm_interval", intervalMillis);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (am != null) {
            am.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarm.getNextAlarmTime(),
                    pendingIntent
            );
        }
    }
}
