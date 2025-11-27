package com.hexakill.medstime;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlarmCardAdapter extends RecyclerView.Adapter<AlarmCardAdapter.AlarmViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(AlarmSet alarmSet);
    }

    private List<AlarmSet> alarmList;
    private OnItemClickListener listener;

    private boolean selectionMode = false;
    private List<AlarmSet> selectedItems = new ArrayList<>();

    public AlarmCardAdapter(List<AlarmSet> alarmList, OnItemClickListener listener) {
        this.alarmList = alarmList;
        this.listener = listener;
    }

    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;
        if (!enabled) selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<AlarmSet> list) {
        this.selectedItems = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmSet alarm = alarmList.get(position);

        // Display medicine name
        holder.tvMedicineName.setText(alarm.getMedicineName());
        // Display next alarm time calculated from interval
        holder.tvNextAlarm.setText(alarm.getNextAlarmTime());
        // Display note
        holder.tvAlarmNote.setText(alarm.getAlarmNote());

        if (selectionMode && selectedItems.contains(alarm)) {
            holder.itemView.setBackgroundResource(R.color.red);
        } else {
            holder.itemView.setBackground(holder.originalBackground);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(alarm);
        });

        // Switch remains displayed but disabled
        holder.reminderSwitch.setChecked(true);
        holder.reminderSwitch.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvNextAlarm, tvAlarmNote;
        Switch reminderSwitch;
        Drawable originalBackground;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvNextAlarm = itemView.findViewById(R.id.tvNextAlarm);
            tvAlarmNote = itemView.findViewById(R.id.tvalarmNote);
            reminderSwitch = itemView.findViewById(R.id.reminderSwitch);
            originalBackground = itemView.getBackground();
        }
    }

    public void updateData(List<AlarmSet> newList) {
        this.alarmList.clear();
        this.alarmList.addAll(newList);
        notifyDataSetChanged(); // redraw RecyclerView
    }


}
