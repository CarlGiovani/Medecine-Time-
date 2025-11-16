package com.hexakill.medstime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MedicineMenuActivity extends AppCompatActivity {

    private List<Medicine> medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_menu);

        // Back button functionality
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Load sample medicines
        medicines = SampleData.getSampleMedicines();

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.medicineRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MedicineAdapter adapter = new MedicineAdapter(medicines, medicine -> {
            // Pass only the medicine name
            Intent modifyIntent = new Intent(MedicineMenuActivity.this, ReminderListActivity.class);
            modifyIntent.putExtra("medicine_name", medicine.getName());
            startActivity(modifyIntent);
        });

        recyclerView.setAdapter(adapter);

        // FAB to create new reminder
        FloatingActionButton addFab = findViewById(R.id.addMedicineFab);
        addFab.setOnClickListener(v -> {
            Intent intent = new Intent(MedicineMenuActivity.this, CreateReminderModal.class);
            startActivity(intent);
        });
    }
}
