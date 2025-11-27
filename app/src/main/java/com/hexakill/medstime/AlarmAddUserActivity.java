package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

public class AlarmAddUserActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText etMedicineName;
    private SeekBar sbInterval;
    private TextView tvIntervalLabel;
    private EditText etNote;
    private FloatingActionButton saveFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_user);

        // Header setup
        HeaderManager.setupHeader(this);

        // Bind views
        backButton = findViewById(R.id.backButton);
        etMedicineName = findViewById(R.id.medicineNameInput);
        sbInterval = findViewById(R.id.everyXSeekBar);
        tvIntervalLabel = findViewById(R.id.everyXSeekBarLabel);
        etNote = findViewById(R.id.noteInput);
        saveFab = findViewById(R.id.saveAlarmFab);

        // Back button
        backButton.setOnClickListener(v -> finish());

        // Populate medicine info from Intent
        String medName = getIntent().getStringExtra("medicine_name");
        etMedicineName.setText(medName != null ? medName : "");

        // Interval SeekBar
        sbInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int interval = Math.max(progress, 1);
                tvIntervalLabel.setText("Interval (hours): " + interval);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Save to database
        saveFab.setOnClickListener(v -> {
            String name = etMedicineName.getText().toString();
            int interval = Math.max(sbInterval.getProgress(), 1);
            String note = etNote.getText().toString();

            MyDbHelper db = new MyDbHelper(this);
            db.addUserReminder(name, "", String.valueOf(interval), note); // description removed

            // Go to HomepageActivity and refresh list
            Intent intent = new Intent(AlarmAddUserActivity.this, HomepageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
