package com.hexakill.medstime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateMedicineActivity extends AppCompatActivity {

    private EditText dosageInput;
    private Button dosageButton, reminderButton;
    private TimePicker timePicker;
    private FloatingActionButton saveMedicineFab;

    private String[] dosageTypes = {"mg - milligram", "ml - milliliters", "g - grams", "tablet", "capsule"};
    private String[] reminderTypes = {"Only Once", "Every X hours", "Daily", "Weekly"};

    private String selectedDosageType = "";
    private String selectedReminderType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_medicine);

        // Setup header/back button
        HeaderManager.setupHeader(this);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Initialize views
        dosageInput = findViewById(R.id.dosageInput);
        dosageButton = findViewById(R.id.dosageButton);
        reminderButton = findViewById(R.id.reminderButton);
        timePicker = findViewById(R.id.timePicker);
        saveMedicineFab = findViewById(R.id.saveMedicineFab);

        // ---------------------------
        // Setup Dialogs for Buttons
        // ---------------------------
        dosageButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Dosage Type")
                    .setItems(dosageTypes, (dialog, which) -> {
                        selectedDosageType = dosageTypes[which];
                        dosageButton.setText(selectedDosageType);
                    })
                    .setNegativeButton("Cancel", null);
            builder.show();
        });

        reminderButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Reminder Type")
                    .setItems(reminderTypes, (dialog, which) -> {
                        selectedReminderType = reminderTypes[which];
                        reminderButton.setText(selectedReminderType);
                    })
                    .setNegativeButton("Cancel", null);
            builder.show();
        });

        // Ensure TimePicker in spinner mode (prevents editable keyboard for AM/PM)
        timePicker.setIs24HourView(false);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

        // Save button
        saveMedicineFab.setOnClickListener(v -> saveMedicine());
    }

    private void saveMedicine() {
        String dosage = dosageInput.getText().toString().trim();

        if (dosage.isEmpty()) {
            Toast.makeText(this, "Please enter dosage", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDosageType.isEmpty()) {
            Toast.makeText(this, "Please select dosage type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedReminderType.isEmpty()) {
            Toast.makeText(this, "Please select reminder type", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // TODO: Save medicine to your database or list here

        Toast.makeText(this, "Medicine saved!", Toast.LENGTH_SHORT).show();

        // Open ReminderListActivity
        Intent intent = new Intent(this, ReminderListActivity.class);
        startActivity(intent);

        // Close current activity
        finish();
    }
}
