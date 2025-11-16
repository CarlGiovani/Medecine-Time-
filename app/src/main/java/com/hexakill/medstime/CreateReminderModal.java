package com.hexakill.medstime;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateReminderModal extends AppCompatActivity {

    private EditText dosageInput, noteInput;
    private Spinner dosageTypeSpinner, reminderTypeSpinner, everyXSpinner;
    private SwitchCompat reminderSwitch;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        // Bind views
        dosageInput = findViewById(R.id.dosageInput);
        noteInput = findViewById(R.id.noteInput);
        dosageTypeSpinner = findViewById(R.id.dosageTypeSpinner);
        reminderTypeSpinner = findViewById(R.id.reminderTypeSpinner);
        everyXSpinner = findViewById(R.id.everyXSpinner);
        reminderSwitch = findViewById(R.id.reminderSwitch); // SwitchCompat
        timePicker = findViewById(R.id.timePicker);

        FloatingActionButton saveFab = findViewById(R.id.saveReminderFab);
        saveFab.setOnClickListener(v -> saveReminder());
    }

    private void saveReminder() {
        String dosage = dosageInput.getText().toString();
        String note = noteInput.getText().toString();
        String dosageType = dosageTypeSpinner.getSelectedItem().toString();
        String reminderType = reminderTypeSpinner.getSelectedItem().toString();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        boolean enabled = reminderSwitch.isChecked();

        // Create a local Reminder object (stored inside this activity)
        Reminder reminder = new Reminder(dosage, dosageType, reminderType, hour, minute, note, enabled);

        // TODO: You can save it to database, shared preferences, or pass it back to the previous activity

        setResult(RESULT_OK); // mark result OK
        finish(); // close activity
    }

    // Simple Reminder model stored inside the same file
    public static class Reminder {
        String dosage, dosageType, reminderType, note;
        int hour, minute;
        boolean enabled;

        public Reminder(String dosage, String dosageType, String reminderType, int hour, int minute, String note, boolean enabled) {
            this.dosage = dosage;
            this.dosageType = dosageType;
            this.reminderType = reminderType;
            this.hour = hour;
            this.minute = minute;
            this.note = note;
            this.enabled = enabled;
        }

        // Optional getters if needed
        public String getDosage() { return dosage; }
        public String getDosageType() { return dosageType; }
        public String getReminderType() { return reminderType; }
        public int getHour() { return hour; }
        public int getMinute() { return minute; }
        public String getNote() { return note; }
        public boolean isEnabled() { return enabled; }
    }
}
