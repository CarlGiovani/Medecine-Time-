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

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Reminder reminder);
    }

    private List<Reminder> reminders;
    private OnItemClickListener listener;

    private boolean selectionMode = false;
    private List<Reminder> selectedItems = new ArrayList<>();

    public ReminderAdapter(List<Reminder> reminders, OnItemClickListener listener) {
        this.reminders = reminders;
        this.listener = listener;
    }

    public void setSelectionMode(boolean enabled) {
        this.selectionMode = enabled;
        if (!enabled) selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setSelectedItems(List<Reminder> list) {
        this.selectedItems = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder r = reminders.get(position);

        holder.tvReminderTime.setText(r.getName());
        holder.tvReminderType.setText(r.getType());

        // Only change background if selected
        if (selectionMode && selectedItems.contains(r)) {
            holder.itemView.setBackgroundResource(R.color.red); // RED from colors.xml
        } else {
            // Restore original drawable
            holder.itemView.setBackground(holder.originalBackground);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(r);
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvReminderTime;
        TextView tvReminderType;
        Drawable originalBackground;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReminderTime = itemView.findViewById(R.id.tvReminderTime);
            tvReminderType = itemView.findViewById(R.id.tvReminderType);
            originalBackground = itemView.getBackground(); // save original drawable
        }
    }
}
