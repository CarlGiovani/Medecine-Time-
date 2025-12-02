package com.hexakill.medstime;

import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.Calendar;

public class AlarmEditPresetActivity extends AppCompatActivity {

    private TextView tvMedicineName, tvIntervalLabel;
    private SeekBar everyXSeekBar;
    private EditText noteInput;
    private TimePicker timePicker;
    private FloatingActionButton saveAlarmFab, deleteAlarmFab;

    private MyDbHelper dbHelper;

    private int alarmId;
    private static final int MIN_INTERVAL = 1; // hours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit_preset);

        HeaderManager.setupHeader(this);

        dbHelper = new MyDbHelper(this);

        // UI
        ImageButton backButton = findViewById(R.id.backButton);
        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvIntervalLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        timePicker = findViewById(R.id.timePicker);
        saveAlarmFab = findViewById(R.id.saveAlarmFab);
        deleteAlarmFab = findViewById(R.id.deleteAlarmFab);

        timePicker.setIs24HourView(false);

        backButton.setOnClickListener(v -> finish());

        // ---------- Read Intent Data ----------
        alarmId = getIntent().getIntExtra("alarm_id", -1);
        String medicineName = getIntent().getStringExtra("medicine_name");
        int interval = getIntent().getIntExtra("alarm_interval", MIN_INTERVAL);
        String note = getIntent().getStringExtra("alarm_note");
        long startTimeMillis = getIntent().getLongExtra("alarm_start_time", -1);

        tvMedicineName.setText(medicineName);
        noteInput.setText(note);

        // Restore time into TimePicker
        if (startTimeMillis > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTimeMillis);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            } else {
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
            }
        }

        // ---------- Interval SeekBar ----------
        everyXSeekBar.setMax(23);
        int safeInterval = Math.max(interval, MIN_INTERVAL);
        everyXSeekBar.setProgress(safeInterval - 1);
        tvIntervalLabel.setText("Interval (hours): " + safeInterval);

        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvIntervalLabel.setText("Interval (hours): " + (progress + 1));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // ---------- SAVE CHANGES ----------
        saveAlarmFab.setOnClickListener(v -> saveChanges(medicineName));

        // ---------- DELETE ----------
        deleteAlarmFab.setOnClickListener(v -> deleteReminder());
    }

    private void saveChanges(String medicineName) {

        int updatedInterval = everyXSeekBar.getProgress() + 1;
        String updatedNote = noteInput.getText().toString();

        // Read time
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
        cal.set(Calendar.MILLISECOND, 0);

        long updatedStartTime = cal.getTimeInMillis();

        if (alarmId != -1) {
            // PREBUILT reminder update
            dbHelper.updatePrebuiltReminder(
                    alarmId,
                    medicineName,
                    "",                                      // desc unused in reminder table
                    String.valueOf(updatedInterval),
                    updatedNote,
                    updatedStartTime
            );

            Toast.makeText(this, "Prebuilt reminder updated!", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    private void deleteReminder() {
        if (alarmId != -1) {
            dbHelper.deletePrebuiltReminder(alarmId);
            Toast.makeText(this, "Prebuilt reminder deleted!", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }
}
