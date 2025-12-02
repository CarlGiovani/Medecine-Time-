package com.hexakill.medstime;

import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hexakill.medstime.database.MyDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlarmCardAdapter extends RecyclerView.Adapter<AlarmCardAdapter.AlarmViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AlarmSet alarmSet);
    }

    private final List<AlarmSet> alarmList;
    private final OnItemClickListener listener;
    private final MyDbHelper dbHelper;

    private boolean selectionMode = false;
    private final List<AlarmSet> selectedItems = new ArrayList<>();

    public AlarmCardAdapter(List<AlarmSet> alarmList, OnItemClickListener listener, MyDbHelper dbHelper) {
        this.alarmList = new ArrayList<>(alarmList);
        this.listener = listener;
        this.dbHelper = dbHelper;
    }

    // -----------------------------------------------------
    // Selection Mode
    // -----------------------------------------------------
    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;
        if (!enabled) selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<AlarmSet> list) {
        selectedItems.clear();
        selectedItems.addAll(list);
        notifyDataSetChanged();
    }

    // -----------------------------------------------------
    // Create ViewHolder
    // -----------------------------------------------------
    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    // -----------------------------------------------------
    // Bind ViewHolder
    // -----------------------------------------------------
    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {

        AlarmSet alarm = alarmList.get(position);

        holder.tvMedicineName.setText(alarm.getMedicineName());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.tvNextAlarm.setText(sdf.format(alarm.getNextAlarmTime()));

        holder.tvAlarmNote.setText(alarm.getAlarmNote());

        updateCardBackground(holder, alarm);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(alarm);
        });

        // SWITCH — disable previous listeners to prevent duplication
        holder.reminderSwitch.setOnCheckedChangeListener(null);
        holder.reminderSwitch.setChecked(alarm.isActive());

        holder.reminderSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            alarm.setActive(isChecked);
            dbHelper.updateAlarmStatus(alarm.getId(), isChecked);
            updateCardBackground(holder, alarm);
        });
    }

    // -----------------------------------------------------
    // Card Background Update Logic
    // -----------------------------------------------------
    private void updateCardBackground(@NonNull AlarmViewHolder holder, @NonNull AlarmSet alarm) {

        Context ctx = holder.itemView.getContext();

        if (selectionMode && selectedItems.contains(alarm)) {
            holder.cardView.setCardBackgroundColor(ctx.getColor(R.color.red));
            holder.tvMedicineName.setTextColor(ctx.getColor(R.color.white));
            holder.tvNextAlarm.setTextColor(ctx.getColor(R.color.white));
            holder.tvAlarmNote.setTextColor(ctx.getColor(R.color.white));
        }
        else if (!alarm.isActive()) {
            holder.cardView.setCardBackgroundColor(ctx.getColor(R.color.red));
            holder.tvMedicineName.setTextColor(ctx.getColor(R.color.black));
            holder.tvNextAlarm.setTextColor(ctx.getColor(R.color.black));
            holder.tvAlarmNote.setTextColor(ctx.getColor(R.color.black));
        }
        else {
            holder.cardView.setCardBackgroundColor(ctx.getColor(R.color.cyan));
            holder.tvMedicineName.setTextColor(ctx.getColor(R.color.white));
            holder.tvNextAlarm.setTextColor(ctx.getColor(R.color.white));
            holder.tvAlarmNote.setTextColor(ctx.getColor(R.color.white));
        }
    }

    // -----------------------------------------------------
    // Count
    // -----------------------------------------------------
    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    // -----------------------------------------------------
    // ViewHolder
    // -----------------------------------------------------
    static class AlarmViewHolder extends RecyclerView.ViewHolder {

        TextView tvMedicineName, tvNextAlarm, tvAlarmNote;
        Switch reminderSwitch;
        MaterialCardView cardView;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvNextAlarm = itemView.findViewById(R.id.tvNextAlarm);
            tvAlarmNote = itemView.findViewById(R.id.tvalarmNote);
            reminderSwitch = itemView.findViewById(R.id.reminderSwitch);
            cardView = (MaterialCardView) itemView; // root layout
        }
    }

    // -----------------------------------------------------
    // DiffUtil Optimized Update
    // -----------------------------------------------------
    public void updateData(List<AlarmSet> newList) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() { return alarmList.size(); }

            @Override
            public int getNewListSize() { return newList.size(); }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return alarmList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                AlarmSet oldItem = alarmList.get(oldItemPosition);
                AlarmSet newItem = newList.get(newItemPosition);

                return oldItem.equals(newItem);
            }
        });

        alarmList.clear();
        alarmList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}
