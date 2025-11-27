package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

public class AlarmAddPresetActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView title;
    private TextView tvMedicineName;
    private TextView tvMedicineDescription;
    private TextView everyXSeekBarLabel;
    private SeekBar everyXSeekBar;
    private EditText noteInput;
    private FloatingActionButton saveAlarmFab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add_preset);

        HeaderManager.setupHeader(this);

        backButton = findViewById(R.id.backButton);
        title = findViewById(R.id.title);
        tvMedicineName = findViewById(R.id.tvMedicineName);
        tvMedicineDescription = findViewById(R.id.tvMedicineDescription);
        everyXSeekBarLabel = findViewById(R.id.everyXSeekBarLabel);
        everyXSeekBar = findViewById(R.id.everyXSeekBar);
        noteInput = findViewById(R.id.noteInput);
        saveAlarmFab = findViewById(R.id.saveAlarmFab);

        title.setText("Add Alarm");

        // Get medicine info from intent
        String medName = getIntent().getStringExtra("medicine_name");
        String medDesc = getIntent().getStringExtra("medicine_description");

        tvMedicineName.setText(medName != null ? medName : "");
        tvMedicineDescription.setText(medDesc != null ? medDesc : "");

        everyXSeekBarLabel.setText("Interval (hours): 1");

        // Back button
        backButton.setOnClickListener(v -> finish());

        // SeekBar listener
        everyXSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int interval = Math.max(progress, 1);
                everyXSeekBarLabel.setText("Interval (hours): " + interval);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Save alarm to database
        saveAlarmFab.setOnClickListener(v -> {
            int interval = Math.max(everyXSeekBar.getProgress(), 1);
            String note = noteInput.getText().toString();

            MyDbHelper db = new MyDbHelper(this);
            db.addPrebuiltReminder(
                    tvMedicineName.getText().toString(),
                    tvMedicineDescription.getText().toString(),
                    interval,
                    note
            );

            setResult(RESULT_OK); // so HomepageActivity can refresh
            Intent intent = new Intent(AlarmAddPresetActivity.this, HomepageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
