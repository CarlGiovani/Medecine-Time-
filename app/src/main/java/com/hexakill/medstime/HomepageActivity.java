package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private List<AlarmSet> alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Header setup
        HeaderManager.setupHeader(this);

        // Load sample alarms
        alarmList = SampleAlarms.getSampleAlarms();

        RecyclerView recyclerView = findViewById(R.id.remindersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter using item_alarm.xml
        AlarmCardAdapter adapter = new AlarmCardAdapter(alarmList, alarmSet -> {
            // Open AlarmEditActivity on click
            Intent intent = new Intent(HomepageActivity.this, AlarmEditActivity.class);
            intent.putExtra("medicine_name", alarmSet.getMedicineName());
            intent.putExtra("medicine_description", alarmSet.getMedicineDescription());
            intent.putExtra("alarm_type", alarmSet.getAlarmType());
            intent.putExtra("alarm_interval", alarmSet.getAlarmInterval());
            intent.putExtra("alarm_note", alarmSet.getAlarmNote());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // FAB to open MedicineListActivity
        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, MedicineListActivity.class);
            startActivity(intent);
        });
    }
}
