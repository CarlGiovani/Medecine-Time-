package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Header setup
        HeaderManager.setupHeader(this);

        // Load sample medicines (only names are needed)
        medicineList = SampleData.getSampleMedicines();

        RecyclerView recyclerView = findViewById(R.id.remindersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter with click listener
        MedicineAdapter adapter = new MedicineAdapter(medicineList, medicine -> {
            Intent intent = new Intent(MainActivity.this, RemindersActivity.class);
            intent.putExtra("medicine_name", medicine.getName()); // pass name only
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // FAB to open MedicineListActivity
        FloatingActionButton addFab = findViewById(R.id.addReminderFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MedicineListActivity.class);
            startActivity(intent);
        });
    }
}
