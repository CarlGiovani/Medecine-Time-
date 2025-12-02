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

public class AlarmEditUserActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView title, everyXSeekBarLabel;
    private EditText medicineNameInput, noteInput;
    private SeekBar everyXSeekBar;
    private TimePicker timePicker;
    private FloatingActionButton saveAlarmFab, deleteAlarmFab;

    private MyDbHelper dbHelper;

    private int alarmId;
    private static final int MIN_INTERVAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit_user);

        HeaderManager.setupHeader(this);

        // UI bindings
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        medicineNameInput = findViewById(R.id.medicineNameInput);
        everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        timePicker = findViewById(R.id.timePicker);
        saveAlarmFab = findViewById(R.id.saveAlarmFab);
        deleteAlarmFab = findViewById(R.id.deleteAlarmFab);

        timePicker.setIs24HourView(false);

        dbHelper = new MyDbHelper(this);
        title.setText("Edit Alarm");

        backButton.setOnClickListener(v -> finish());

        // -------- Get intent data --------
        alarmId = getIntent().getIntExtra("alarm_id", -1);
        String alarmName = getIntent().getStringExtra("medicine_name");
        int alarmInterval = getIntent().getIntExtra("alarm_interval", MIN_INTERVAL);
        String alarmNote = getIntent().getStringExtra("alarm_note");
        long startTimeMillis = getIntent().getLongExtra("alarm_start_time", -1);

        medicineNameInput.setText(alarmName);
        noteInput.setText(alarmNote);

        // -------- Restore time --------
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

        // -------- Interval SeekBar --------
        everyXSeekBar.setMax(23);
        int safeInterval = Math.max(alarmInterval, MIN_INTERVAL);
        everyXSeekBar.setProgress(safeInterval - 1);
        everyXSeekBarLabel.setText("Interval (hours): " + safeInterval);

        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                everyXSeekBarLabel.setText("Interval (hours): " + (progress + 1));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // -------- SAVE CHANGES --------
        saveAlarmFab.setOnClickListener(v -> saveChanges());

        // -------- DELETE REMINDER --------
        deleteAlarmFab.setOnClickListener(v -> deleteReminder());
    }


    // ============================================================
    // SAVE USER REMINDER CHANGES
    // ============================================================
    private void saveChanges() {

        String updatedName = medicineNameInput.getText().toString().trim();
        if (updatedName.isEmpty()) {
            medicineNameInput.setError("Name is required");
            medicineNameInput.requestFocus();
            return;
        }

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
            // USER reminder update
            dbHelper.updateUserReminder(
                    alarmId,
                    updatedName,
                    "", // description unused
                    String.valueOf(updatedInterval),
                    updatedNote,
                    updatedStartTime
            );

            Toast.makeText(this, "Alarm updated!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: Invalid alarm ID", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }

    // ============================================================
    // DELETE USER REMINDER
    // ============================================================
    private void deleteReminder() {
        if (alarmId != -1) {
            dbHelper.deleteUserReminder(alarmId);
            Toast.makeText(this, "Alarm deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Delete failed: invalid ID", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }
}
