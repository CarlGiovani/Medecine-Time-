package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlarmEditActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView tvMedicineName, tvMedicineDescription;
    private Button alarmTypeButton;
    private SeekBar everyXSeekBar;
    private EditText noteInput;
    private FloatingActionButton saveAlarmFab, deleteAlarmFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        // Setup header (if you have a HeaderManager helper)
        HeaderManager.setupHeader(this);

        // Back button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Read-only medicine info
        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineDescription = findViewById(R.id.tvMedicineDescription);

        // Alarm settings
        alarmTypeButton = findViewById(R.id.alarmTypeButton);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);

        // FABs
        saveAlarmFab = findViewById(R.id.saveAlarmFab);
        deleteAlarmFab = findViewById(R.id.deleteAlarmFab);

        // Receive AlarmSet data from intent
        String medicineName = getIntent().getStringExtra("medicine_name");
        String medicineDescription = getIntent().getStringExtra("medicine_description");
        String alarmType = getIntent().getStringExtra("alarm_type");
        int alarmInterval = getIntent().getIntExtra("alarm_interval", 0);
        String alarmNote = getIntent().getStringExtra("alarm_note");

        // Populate fields
        tvMedicineName.setText(medicineName != null ? medicineName : "");
        tvMedicineDescription.setText(medicineDescription != null ? medicineDescription : "");
        alarmTypeButton.setText(alarmType != null ? alarmType : "- Select -");
        everyXSeekBar.setProgress(alarmInterval);
        noteInput.setText(alarmNote != null ? alarmNote : "");

        // Alarm type button click
        alarmTypeButton.setOnClickListener(v -> {
            // Show dialog or dropdown to select alarm type
        });

        // Save alarm
        saveAlarmFab.setOnClickListener(v -> {
            String updatedType = alarmTypeButton.getText().toString();
            int updatedInterval = everyXSeekBar.getProgress();
            String updatedNote = noteInput.getText().toString();

            // TODO: save or update AlarmSet in backend / database

            finish();
        });

        // Delete alarm
        deleteAlarmFab.setOnClickListener(v -> {
            // TODO: delete alarm from backend / database

            finish();
        });
    }
}
