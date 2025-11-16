package com.hexakill.medstime;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Header setup
        HeaderManager.setupHeader(this);

        // 1st Button: Ringtone
        Button btnRingtone = findViewById(R.id.btnRingtone);
        btnRingtone.setOnClickListener(v ->
                Toast.makeText(this, "Ringtone", Toast.LENGTH_SHORT).show()
        );

        // 2nd: Dropdown Auto Silent
        Spinner spinnerAutoSilent = findViewById(R.id.spinnerAutoSilent);
        String[] options = {"Off", "15 min", "30 min", "1 hour"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinnerAutoSilent.setAdapter(adapter);
        spinnerAutoSilent.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SettingsActivity.this, "Auto Silent: " + options[position], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // 3rd Button: Snooze
        Button btnSnooze = findViewById(R.id.btnSnooze);
        btnSnooze.setOnClickListener(v ->
                Toast.makeText(this, "Snooze", Toast.LENGTH_SHORT).show()
        );

        // 4th Switch: Notification before ringing
        Switch switchNotification = findViewById(R.id.switchNotification);
        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "Notification before ringing: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show()
        );

        // 5th Switch: Allow Permission
        Switch switchPermission = findViewById(R.id.switchPermission);
        switchPermission.setOnCheckedChangeListener((buttonView, isChecked) ->
                Toast.makeText(this, "Allow Permission: " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show()
        );

        // Back button functionality
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
}
