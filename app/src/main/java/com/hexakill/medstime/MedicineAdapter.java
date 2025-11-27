package com.hexakill.medstime;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Medicine medicine);
    }

    private final List<Medicine> medicineList;
    private final OnItemClickListener listener;

    private boolean selectionMode = false;
    private List<Medicine> selectedItems = new ArrayList<>();

    public MedicineAdapter(List<Medicine> medicineList, OnItemClickListener listener) {
        this.medicineList = medicineList;
        this.listener = listener;
    }

    // Update list dynamically
    public void updateData(List<Medicine> newList) {
        this.medicineList.clear();
        this.medicineList.addAll(newList);
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean selectionMode) {
        this.selectionMode = selectionMode;
        if (!selectionMode) selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<Medicine> selectedItems) {
        this.selectedItems = selectedItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.nameText.setText(medicine.getName());
        holder.descriptionText.setText(medicine.getDescription()); // display description

        // Highlight selected items only in selection mode
        if (selectionMode && selectedItems.contains(medicine)) {
            holder.itemView.setBackgroundResource(R.color.red); // replace with your color
        } else {
            holder.itemView.setBackground(holder.originalBackground);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(medicine));
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText; // added for description
        Drawable originalBackground;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.tvMedicineName);
            descriptionText = itemView.findViewById(R.id.tvMedicineDescription); // make sure exists in XML
            originalBackground = itemView.getBackground();
        }
    }
}
