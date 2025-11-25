package com.hexakill.medstime;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlarmAddPresetActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView title;
    private TextView tvMedicineName;
    private TextView tvMedicineDescription;
    private TextView alarmTypeLabel;
    private Button alarmTypeButton;
    private TextView everyXSeekBarLabel;
    private SeekBar everyXSeekBar;
    private EditText noteInput;
    private FloatingActionButton saveAlarmFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_preset); // make sure this matches your XML filename
        // Header setup
        HeaderManager.setupHeader(this);

        // Initialize views
        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineDescription = findViewById(R.id.tvMedicineDescription);
        alarmTypeLabel = findViewById(R.id.alarmTypeLabel);
        alarmTypeButton = findViewById(R.id.alarmTypeButton);
        everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        saveAlarmFab = findViewById(R.id.saveAlarmFab);

        // Set default texts (optional, can be dynamic)
        title.setText("Add Alarm");
        tvMedicineName.setText("Paracetamol");
        tvMedicineDescription.setText("Sample description of the medicine.");
        alarmTypeButton.setText("Only Once");
        everyXSeekBarLabel.setText("Interval (hours): 1");

        // Back button click listener
        backButton.setOnClickListener(v -> finish());

        // Alarm type button click listener
        alarmTypeButton.setOnClickListener(v -> {
            // Example toggle logic
            if ("Only Once".equals(alarmTypeButton.getText().toString())) {
                alarmTypeButton.setText("Daily");
            } else {
                alarmTypeButton.setText("Only Once");
            }
        });

        // SeekBar listener
        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int interval = progress > 0 ? progress : 1; // minimum 1 hour
                everyXSeekBarLabel.setText("Interval (hours): " + interval);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Save FAB click listener
        saveAlarmFab.setOnClickListener(v -> {
            // Gather values
            String note = noteInput.getText().toString();
            String alarmType = alarmTypeButton.getText().toString();
            int interval = everyXSeekBar.getProgress() > 0 ? everyXSeekBar.getProgress() : 1;

            // TODO: Save alarm logic here

            // Close activity after saving
            finish();
        });
    }
}
