/*
package com.hexakill.medstime;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MedicineMenuActivity extends AppCompatActivity {

    private List<Medicine> medicines;
    private List<Medicine> selectedItems = new ArrayList<>();
    private MedicineAdapter adapter;

    private boolean selectionMode = false;

    private FloatingActionButton deleteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_menu);

        HeaderManager.setupHeader(this);

        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Load sample medicines
        medicines = SampleData.getSampleMedicines();

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.medicineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MedicineAdapter(medicines, medicine -> {
            if (!selectionMode) {
                Intent modifyIntent = new Intent(MedicineMenuActivity.this, EditMedicineActivity.class);
                modifyIntent.putExtra("medicine_name", medicine.getName());
                startActivity(modifyIntent);
            } else {
                toggleSelection(medicine);
            }
        });

        recyclerView.setAdapter(adapter);

        // FAB to create new medicine
        FloatingActionButton addFab = findViewById(R.id.addMedicineFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MedicineMenuActivity.this, AddMedicineActivity.class);
            startActivity(intent);
        });

        // Mark / select button
        ImageButton markButton = findViewById(R.id.markButton);
        markButton.setOnClickListener(v -> toggleSelectionMode());

        // Delete FAB
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

    private void toggleSelection(Medicine medicine) {
        if (selectedItems.contains(medicine)) {
            selectedItems.remove(medicine);
        } else {
            selectedItems.add(medicine);
        }
        adapter.setSelectedItems(selectedItems);
    }

    private void showDeleteConfirmation() {
        if (selectedItems.isEmpty()) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete Selected Medicines")
                .setMessage("Are you sure you want to delete the selected medicines?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    medicines.removeAll(selectedItems);
                    selectedItems.clear();
                    adapter.notifyDataSetChanged();
                    toggleSelectionMode();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
*/
