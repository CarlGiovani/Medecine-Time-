package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MedicineListActivity extends AppCompatActivity {

    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);


        HeaderManager.setupHeader(this);

        // Load sample medicines
        medicineList = SampleData.getSampleMedicines();

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.medicineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MedicineAdapter adapter = new MedicineAdapter(medicineList, medicine -> {
            // Open ReminderListActivity and pass medicine name
            Intent intent = new Intent(MedicineListActivity.this, RemindersActivity.class);
            intent.putExtra("medicine_name", medicine.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // FAB to create new reminder
        FloatingActionButton addFab = findViewById(R.id.addMedicineFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MedicineListActivity.this, AddMedicineActivity.class);
            startActivity(intent);
        });
    }
}
