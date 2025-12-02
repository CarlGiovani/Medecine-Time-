package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmCardAdapter adapter;
    private MyDbHelper dbHelper;

    private List<AlarmSet> allAlarms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        HeaderManager.setupHeader(this);

        dbHelper = new MyDbHelper(this);

        recyclerView = findViewById(R.id.remindersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AlarmCardAdapter(new ArrayList<>(), this::onAlarmClicked, dbHelper);
        recyclerView.setAdapter(adapter);

        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, MedicineListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlarms();
    }

    // ============================================================
    // LOAD ALL REMINDERS (USER + PREBUILT)
    // ============================================================
    private void loadAlarms() {
        List<AlarmSet> userAlarms = dbHelper.getAllUserReminders();
        for (AlarmSet alarm : userAlarms) {
            alarm.computeNextAlarmTime();
            alarm.setUserCreated(true);
        }

        List<AlarmSet> prebuiltAlarms = dbHelper.getAllPremadeReminders();
        for (AlarmSet alarm : prebuiltAlarms) {
            alarm.computeNextAlarmTime();
            alarm.setUserCreated(false);
        }

        allAlarms.clear();
        allAlarms.addAll(userAlarms);
        allAlarms.addAll(prebuiltAlarms);

        adapter.updateData(allAlarms);
    }


    // ============================================================
    // OPEN EDIT SCREEN
    // ============================================================
    private void onAlarmClicked(AlarmSet alarmSet) {

        Intent intent;

        if (alarmSet.isUserCreated()) {
            intent = new Intent(this, AlarmEditUserActivity.class);
        } else {
            intent = new Intent(this, AlarmEditPresetActivity.class);
        }

        intent.putExtra("alarm_id", alarmSet.getId());
        intent.putExtra("medicine_name", alarmSet.getMedicineName());
        intent.putExtra("alarm_interval", Integer.parseInt(alarmSet.getAlarmInterval()));
        intent.putExtra("alarm_note", alarmSet.getAlarmNote());
        intent.putExtra("alarm_start_time", alarmSet.getStartTime());

        startActivity(intent);
    }
}
