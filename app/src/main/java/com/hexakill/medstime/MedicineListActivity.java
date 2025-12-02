package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hexakill.medstime.database.MyDbHelper;

import java.util.List;

public class MedicineListActivity extends AppCompatActivity {

    private List<Medicine> medicineList;
    private MedicineAdapter adapter;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        HeaderManager.setupHeader(this);

        dbHelper = new MyDbHelper(this);

        RecyclerView recyclerView = findViewById(R.id.medicineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load prebuilt list
        loadPrebuiltMedicines();

        // Set adapter
        adapter = new MedicineAdapter(medicineList, medicine -> {
            Intent intent = new Intent(MedicineListActivity.this, AlarmAddPresetActivity.class);
            intent.putExtra("medicine_name", medicine.getName());
            intent.putExtra("medicine_description", medicine.getDescription());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Add user-created reminder
        FloatingActionButton addFab = findViewById(R.id.addMedicineFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MedicineListActivity.this, AlarmAddUserActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPrebuiltMedicines();

        if (adapter != null) {
            adapter.updateData(medicineList);
        }
    }

    // Load prebuilt medicines from the new database structure
    private void loadPrebuiltMedicines() {
        medicineList = dbHelper.getAllPrebuiltMedicines();
    }
}
