package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlarmAddUserActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText etMedicineName;
    private EditText etMedicineDescription;
    private Button btnAlarmType;
    private SeekBar sbInterval;
    private TextView tvIntervalLabel;
    private EditText etNote;
    private FloatingActionButton saveFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_user);

        // Header setup (assuming you have a HeaderManager helper)
        HeaderManager.setupHeader(this);

        // Bind views
        backButton = findViewById(R.id.backButton);
        etMedicineName = findViewById(R.id.medicineNameInput);
        etMedicineDescription = findViewById(R.id.medicineDescriptionInput);
        btnAlarmType = findViewById(R.id.alarmTypeButton);
        sbInterval = findViewById(R.id.everyXSeekBar);
        tvIntervalLabel = findViewById(R.id.everyXSeekBarLabel);
        etNote = findViewById(R.id.noteInput);
        saveFab = findViewById(R.id.saveAlarmFab);

        // Back button click listener
        backButton.setOnClickListener(v -> finish());

        // Populate medicine info if passed via Intent
        String medicineName = getIntent().getStringExtra("medicine_name");
        String medicineDesc = getIntent().getStringExtra("medicine_description");
        if (medicineName != null) etMedicineName.setText(medicineName);
        if (medicineDesc != null) etMedicineDescription.setText(medicineDesc);

        // Update interval label dynamically
        sbInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvIntervalLabel.setText("Interval (hours): " + (progress + 1));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Save FAB click listener
        saveFab.setOnClickListener(v -> {
            String medicineNameInput = etMedicineName.getText().toString();
            String medicineDescInput = etMedicineDescription.getText().toString();
            String alarmType = btnAlarmType.getText().toString();
            int interval = sbInterval.getProgress() + 1;
            String note = etNote.getText().toString();

            // TODO: Save data to database or pass back to previous activity

            finish();
        });
    }
}
