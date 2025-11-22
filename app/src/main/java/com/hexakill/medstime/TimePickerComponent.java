package com.hexakill.medstime;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

public class TimePickerComponent extends LinearLayout {

    private TimePicker timePicker;

    public TimePickerComponent(Context context) {
        super(context);
        init(context);
    }

    public TimePickerComponent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimePickerComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.time_picker_component, this, true);
        timePicker = findViewById(R.id.timePicker);

        // Spinner mode and disable keyboard input
        timePicker.setIs24HourView(false);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public int getHour() {
        return timePicker.getHour();
    }

    public int getMinute() {
        return timePicker.getMinute();
    }

    public void setTime(int hour, int minute) {
        timePicker.setHour(hour);
        timePicker.setMinute(minute);
    }
}
