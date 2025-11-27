package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

public class AlarmEditActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView tvMedicineName, tvIntervalLabel;
    private SeekBar everyXSeekBar;
    private EditText noteInput;
    private FloatingActionButton saveAlarmFab, deleteAlarmFab;

    private MyDbHelper dbHelper;
    private int alarmId; // Store the ID of the current alarm
    private static final int MIN_INTERVAL = 1; // minimum 1 hour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        HeaderManager.setupHeader(this);

        // Bind views
        backButton = findViewById(R.id.backButton);
        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvIntervalLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        saveAlarmFab = findViewById(R.id.saveAlarmFab);
        deleteAlarmFab = findViewById(R.id.deleteAlarmFab);

        dbHelper = new MyDbHelper(this);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Receive AlarmSet data from intent
        alarmId = getIntent().getIntExtra("alarm_id", -1);
        String medicineName = getIntent().getStringExtra("medicine_name");
        int alarmInterval = getIntent().getIntExtra("alarm_interval", MIN_INTERVAL);
        String alarmNote = getIntent().getStringExtra("alarm_note");

        // Populate fields
        tvMedicineName.setText(medicineName != null ? medicineName : "");
        everyXSeekBar.setMax(24); // optional max interval
        everyXSeekBar.setProgress(Math.max(alarmInterval, MIN_INTERVAL));
        tvIntervalLabel.setText("Interval (hours): " + everyXSeekBar.getProgress());
        noteInput.setText(alarmNote != null ? alarmNote : "");

        // SeekBar listener to update label
        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int interval = Math.max(progress, MIN_INTERVAL);
                tvIntervalLabel.setText("Interval (hours): " + interval);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Save alarm
        saveAlarmFab.setOnClickListener(v -> {
            int updatedInterval = Math.max(everyXSeekBar.getProgress(), MIN_INTERVAL);
            String updatedNote = noteInput.getText().toString();

            if (alarmId != -1) {
                // Update existing alarm
                dbHelper.updateUserReminder(alarmId, medicineName, "",
                        String.valueOf(updatedInterval), updatedNote);
            } else {
                // Insert new alarm
                dbHelper.addUserReminder(medicineName, "",
                        String.valueOf(updatedInterval), updatedNote);
            }

            setResult(RESULT_OK); // notify HomepageActivity to refresh
            finish();
        });

        // Delete alarm
        deleteAlarmFab.setOnClickListener(v -> {
            if (alarmId != -1) {
                dbHelper.deleteUserReminder(alarmId);
                setResult(RESULT_OK); // notify HomepageActivity to refresh
            }
            finish();
        });
    }
}
