/*
    package com.hexakill.medstime;

    import android.graphics.Color;
    import android.os.Bundle;
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

    public class CreateMedicineActivity extends AppCompatActivity {

        private EditText medicineNameInput, dosageInput;
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
                "Daily"
        };

        private String selectedDosageType = "";
        private String selectedReminderType = "";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_reminder);

            medicineNameInput = findViewById(R.id.medicineNameInput);
            dosageInput = findViewById(R.id.dosageInput);
            dosageButton = findViewById(R.id.dosageButton);
            reminderButton = findViewById(R.id.reminderButton);
            timePicker = findViewById(R.id.timePicker);
            everyXSeekBarRow = findViewById(R.id.everyXSeekBarRow);
            everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
            everyXSeekBar = findViewById(R.id.everyXSeekBar);

            dosageButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle("Select Dosage Type")
                        .setItems(dosageTypes, (dialog, which) -> {
                            selectedDosageType = dosageTypes[which];
                            dosageButton.setText(selectedDosageType);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            reminderButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
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
                        .show();
            });

            everyXSeekBar.setProgress(0);
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

            timePicker.setIs24HourView(false);
            timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
            setTimePickerTextColor(timePicker, Color.BLACK);

            findViewById(R.id.saveMedicineFab).setOnClickListener(v -> saveMedicine());
        }

        private void saveMedicine() {
            String name = medicineNameInput.getText().toString().trim();
            String dosage = dosageInput.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter medicine name", Toast.LENGTH_SHORT).show();
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

            Toast.makeText(this, "Medicine saved!", Toast.LENGTH_SHORT).show();
            finish();
        }

        private void setTimePickerTextColor(TimePicker tp, int color) {
            try {
                LinearLayout ll = (LinearLayout) tp.getChildAt(0);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    Object child = ll.getChildAt(i);
                    if (child instanceof NumberPicker) {
                        NumberPicker np = (NumberPicker) child;
                        for (int j = 0; j < np.getChildCount(); j++) {
                            if (np.getChildAt(j) instanceof EditText) {
                                ((EditText) np.getChildAt(j)).setTextColor(color);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/
