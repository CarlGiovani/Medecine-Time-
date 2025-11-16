package com.hexakill.medstime;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ReminderListActivity extends AppCompatActivity {

    private List<Reminder> reminderList;
    private List<Reminder> selectedItems = new ArrayList<>();
    private ReminderAdapter adapter;

    private boolean selectionMode = false;

    private FloatingActionButton deleteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);

        HeaderManager.setupHeader(this);

        String medicineName = getIntent().getStringExtra("medicine_name");

        TextView tvMedicineName = findViewById(R.id.tvMedicineName);
        if (tvMedicineName != null && medicineName != null) {
            tvMedicineName.setText(medicineName);
        }

        reminderList = SampleData.getSampleReminders();

        RecyclerView recyclerView = findViewById(R.id.reminderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReminderAdapter(reminderList, reminder -> {
            if (!selectionMode) {
                Intent intent = new Intent(ReminderListActivity.this, ModifyReminderModal.class);
                intent.putExtra("medicine_name", medicineName);
                startActivity(intent);
            } else {
                toggleSelection(reminder);
            }
        });

        recyclerView.setAdapter(adapter);

        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(ReminderListActivity.this, CreateReminderModal.class);
            intent.putExtra("medicine_name", medicineName);
            startActivity(intent);
        });

        ImageButton markButton = findViewById(R.id.markButton);
        markButton.setOnClickListener(v -> toggleSelectionMode());

        deleteFab = findViewById(R.id.deleteSelectedFab);
        deleteFab.setVisibility(View.GONE);
        deleteFab.setOnClickListener(v -> showDeleteConfirmation());
    }

    private void toggleSelectionMode() {
        selectionMode = !selectionMode;

        if (!selectionMode) selectedItems.clear();

        adapter.setSelectionMode(selectionMode);
        adapter.setSelectedItems(selectedItems);

        deleteFab.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
    }

    private void toggleSelection(Reminder reminder) {
        if (selectedItems.contains(reminder)) {
            selectedItems.remove(reminder);
        } else {
            selectedItems.add(reminder);
        }

        adapter.setSelectedItems(selectedItems);
    }

    private void showDeleteConfirmation() {
        if (selectedItems.isEmpty()) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete Selected Reminders")
                .setMessage("Are you sure you want to delete the selected reminders?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    reminderList.removeAll(selectedItems);
                    selectedItems.clear();
                    adapter.notifyDataSetChanged();
                    toggleSelectionMode();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
