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

public class RemindersActivity extends AppCompatActivity {

    private List<Reminder> reminderList;
    private List<Reminder> selectedItems = new ArrayList<>();
    private RemindersAdapter adapter;

    private boolean selectionMode = false;
    private FloatingActionButton deleteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        HeaderManager.setupHeader(this);

        String medicineName = getIntent().getStringExtra("medicine_name");

        TextView tvMedicineName = findViewById(R.id.tvMedicineName);
        if (tvMedicineName != null && medicineName != null) {
            tvMedicineName.setText(medicineName);
        }

        reminderList = SampleData.getSampleReminders();

        RecyclerView recyclerView = findViewById(R.id.reminderRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter with click listener
        adapter = new RemindersAdapter(reminderList, reminder -> {
            if (!selectionMode) {
                // Open EditReminderActivity
                Intent intent = new Intent(RemindersActivity.this, EditReminderActivity.class);
                intent.putExtra("medicine_name", medicineName);
                intent.putExtra("reminder", reminder); // Reminder is Serializable
                startActivity(intent);
            } else {
                // Toggle selection if selectionMode is on
                toggleSelection(reminder);
            }
        });

        recyclerView.setAdapter(adapter);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // FAB to add reminder
        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(RemindersActivity.this, AddReminderActivity.class);
            intent.putExtra("medicine_name", medicineName);
            startActivity(intent);
        });

        // Mark button for selection mode
        ImageButton markButton = findViewById(R.id.markButton);
        markButton.setOnClickListener(v -> toggleSelectionMode());

        // Delete selected reminders FAB
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
