package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.Toast;

public class ModifyMedicineModal extends AppCompatActivity {

    private EditText dosageInput;
    private Spinner dosageTypeSpinner, reminderTypeSpinner, everyXSpinner;
    private SwitchCompat medicineSwitch;
    private TimePicker timePicker;
    private FloatingActionButton saveMedicineFab, deleteMedicineFab;

    private String medicineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_medicine);

        // Initialize views
        medicineSwitch = findViewById(R.id.medicineSwitch);
        dosageInput = findViewById(R.id.dosageInput);
        dosageTypeSpinner = findViewById(R.id.dosageTypeSpinner);
        reminderTypeSpinner = findViewById(R.id.reminderTypeSpinner);
        everyXSpinner = findViewById(R.id.everyXSpinner);
        timePicker = findViewById(R.id.timePicker);
        saveMedicineFab = findViewById(R.id.saveMedicineFab);
        deleteMedicineFab = findViewById(R.id.deleteMedicineFab);

        // Get medicine name from intent
        medicineName = getIntent().getStringExtra("medicine_name");
        if (medicineName != null) {
            prefillMedicineData(medicineName);
        }

        // Save button
        saveMedicineFab.setOnClickListener(v -> saveMedicine());

        // Delete button
        deleteMedicineFab.setOnClickListener(v -> deleteMedicine());
    }

    private void prefillMedicineData(String name) {
        // TODO: Load medicine data from your database or list
        dosageInput.setText("500"); // Example
        medicineSwitch.setChecked(true);
        timePicker.setHour(8);
        timePicker.setMinute(0);
        // TODO: Set spinner selections based on the medicine data
    }

    private void saveMedicine() {
        String dosage = dosageInput.getText().toString().trim();
        String dosageType = dosageTypeSpinner.getSelectedItem().toString();
        String reminderType = reminderTypeSpinner.getSelectedItem().toString();
        int everyX = Integer.parseInt(everyXSpinner.getSelectedItem().toString());
        boolean isActive = medicineSwitch.isChecked();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        if (dosage.isEmpty()) {
            Toast.makeText(this, "Please enter dosage", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Update medicine in your database or list
        Toast.makeText(this, "Medicine updated!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void deleteMedicine() {
        // TODO: Remove medicine from your database or list
        Toast.makeText(this, medicineName + " deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
