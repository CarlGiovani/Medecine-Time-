package com.hexakill.medstime;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class AddReminderActivity extends AppCompatActivity {

    private EditText reminderNameInput, dosageInput, noteInput;
    private Button dosageButton, reminderButton;
    private TimePicker timePicker;
    private LinearLayout everyXSeekBarRow;
    private TextView everyXSeekBarLabel;
    private SeekBar everyXSeekBar;
    private int everyXValue = 2;

    private String[] dosageTypes = {
            "mg - milligram",
            "ml - milliliters",
            "g - grams",
            "tablet",
            "capsule"
    };
    private String[] reminderTypes = {
            "Only Once",
            "Every X hours",
            "Daily",
            "Weekly"
    };

    private String selectedDosageType = "";
    private String selectedReminderType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        HeaderManager.setupHeader(this);
        reminderNameInput = findViewById(R.id.medicineNameInput);
        dosageInput = findViewById(R.id.dosageInput);
        noteInput = findViewById(R.id.noteInput);
        dosageButton = findViewById(R.id.dosageButton);
        reminderButton = findViewById(R.id.reminderButton);
        timePicker = findViewById(R.id.timePicker);
        everyXSeekBarRow = findViewById(R.id.everyXSeekBarRow);
        everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);

        // Dosage type selection
        dosageButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Select Dosage Type")
                .setItems(dosageTypes, (dialog, which) -> {
                    selectedDosageType = dosageTypes[which];
                    dosageButton.setText(selectedDosageType);
                })
                .setNegativeButton("Cancel", null)
                .show()
        );

        // Reminder type selection
        reminderButton.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Select Reminder Type")
                .setItems(reminderTypes, (dialog, which) -> {
                    selectedReminderType = reminderTypes[which];
                    reminderButton.setText(selectedReminderType);
                    if ("Every X hours".equals(selectedReminderType)) {
                        everyXSeekBarRow.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        everyXSeekBarRow.setVisibility(LinearLayout.GONE);
                        everyXValue = 2;
                        everyXSeekBar.setProgress(0);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show()
        );

        // SeekBar initialization
        everyXSeekBar.setProgress(everyXValue - 2);
        everyXSeekBarLabel.setText("Set hours interval: " + everyXValue);
        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                everyXValue = progress + 2;
                everyXSeekBarLabel.setText("Set hours interval: " + everyXValue);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // TimePicker configuration
        timePicker.setIs24HourView(false);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePicker.setHour(8);   // default hour
        timePicker.setMinute(0); // default minute
        setTimePickerTextColor(timePicker, Color.BLACK);

        findViewById(R.id.saveMedicineFab).setOnClickListener(v -> saveReminder());
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void saveReminder() {
        String name = reminderNameInput.getText().toString().trim();
        String dosage = dosageInput.getText().toString().trim();
        String note = noteInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter reminder name", Toast.LENGTH_SHORT).show();
            return;
        }
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

        // TODO: Save the new reminder to database or pass back to previous activity
        Toast.makeText(this, "Reminder added!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // --- SAFER METHOD TO CHANGE HOUR, MINUTE, AND AM/PM TEXT COLOR ---
    private void setTimePickerTextColor(TimePicker tp, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < tp.getChildCount(); i++) {
                View child = tp.getChildAt(i);
                if (child instanceof NumberPicker) {
                    setNumberPickerTextColor((NumberPicker) child, color);
                }
            }
        }
    }

    private void setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        try {
            Field[] fields = NumberPicker.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("mSelectorWheelPaint")) {
                    Paint paint = (Paint) field.get(numberPicker);
                    paint.setColor(color);
                    numberPicker.invalidate();
                }
                if (field.getName().equals("mInputText")) {
                    EditText editText = (EditText) field.get(numberPicker);
                    editText.setTextColor(color);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
