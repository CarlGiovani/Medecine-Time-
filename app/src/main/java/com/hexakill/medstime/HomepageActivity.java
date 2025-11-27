package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    private List<AlarmSet> alarmList;
    private RecyclerView recyclerView;
    private AlarmCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Header setup
        HeaderManager.setupHeader(this);

        // RecyclerView setup
        recyclerView = findViewById(R.id.remindersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load alarms initially
        loadAlarmsFromDatabase();

        // FAB to open MedicineListActivity
        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(HomepageActivity.this, MedicineListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Refresh alarms when returning from Add Alarm
        loadAlarmsFromDatabase();
    }

    private void loadAlarmsFromDatabase() {
        MyDbHelper dbHelper = new MyDbHelper(this);
        alarmList = dbHelper.getAllUserReminders(); // fetch latest alarms

        if (adapter == null) {
            // First time setup
            adapter = new AlarmCardAdapter(alarmList, alarmSet -> {
                Intent intent = new Intent(HomepageActivity.this, AlarmEditActivity.class);
                intent.putExtra("alarm_id", alarmSet.getId());
                intent.putExtra("medicine_name", alarmSet.getMedicineName());
                intent.putExtra("alarm_interval", Integer.parseInt(alarmSet.getAlarmInterval()));
                intent.putExtra("alarm_note", alarmSet.getAlarmNote());
                startActivityForResult(intent, 100);
            });



            recyclerView.setAdapter(adapter);
        } else {
            // Update existing adapter with new data
            adapter.updateData(alarmList);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadAlarmsFromDatabase(); // reload alarms and update RecyclerView
        }
    }

}
