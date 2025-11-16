package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ModifyReminderModal extends AppCompatActivity {

    private EditText dosageInput, noteInput;
    private Spinner dosageTypeSpinner, reminderTypeSpinner, everyXSpinner;
    private Switch reminderSwitch;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_reminder);

        // Bind views
        dosageInput = findViewById(R.id.dosageInput);
        noteInput = findViewById(R.id.noteInput);
        dosageTypeSpinner = findViewById(R.id.dosageTypeSpinner);
        reminderTypeSpinner = findViewById(R.id.reminderTypeSpinner);
        everyXSpinner = findViewById(R.id.everyXSpinner);
        reminderSwitch = findViewById(R.id.reminderSwitch);
        timePicker = findViewById(R.id.timePicker);

        FloatingActionButton saveFab = findViewById(R.id.saveReminderFab);
        FloatingActionButton deleteFab = findViewById(R.id.deleteReminderFab);

        saveFab.setOnClickListener(v -> modifyReminder());
        deleteFab.setOnClickListener(v -> deleteReminder());

        loadReminder();
    }

    private void loadReminder() {
        // Demo: populate fields with a sample reminder
        dosageInput.setText("500");
        noteInput.setText("Take after meal");
        // Set spinners and timePicker as needed
        reminderSwitch.setChecked(true);
    }

    private void modifyReminder() {
        // Save modifications (store locally for now)
        setResult(RESULT_OK);
        finish();
    }

    private void deleteReminder() {
        // Remove reminder (just finish in this demo)
        setResult(RESULT_CANCELED);
        finish();
    }
}
