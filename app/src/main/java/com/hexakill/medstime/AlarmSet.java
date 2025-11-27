package com.hexakill.medstime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmSet {
    private int id;
    private String medicineName;
    private String alarmInterval; // interval in minutes
    private String alarmNote;
    private long startTimeMillis; // timestamp of when alarm was first set or last triggered

    // Constructor including ID
    public AlarmSet(int id, String medicineName, String alarmInterval, String alarmNote, long startTimeMillis) {
        this.id = id;
        this.medicineName = medicineName;
        this.alarmInterval = alarmInterval;
        this.alarmNote = alarmNote;
        this.startTimeMillis = startTimeMillis;
    }

    // Constructor without ID (new alarm)
    public AlarmSet(String medicineName, String alarmInterval, String alarmNote) {
        this(-1, medicineName, alarmInterval, alarmNote, System.currentTimeMillis());
    }

    public int getId() { return id; }
    public String getMedicineName() { return medicineName; }
    public String getAlarmInterval() { return alarmInterval; }
    public String getAlarmNote() { return alarmNote; }
    public long getStartTimeMillis() { return startTimeMillis; }
    public void setStartTimeMillis(long millis) { this.startTimeMillis = millis; }

    /**
     * Calculates the next alarm time based on startTimeMillis and interval.
     * Loops forward until the time is in the future.
     */
    public String getNextAlarmTime() {
        try {
            int intervalMinutes = Integer.parseInt(alarmInterval);
            if (intervalMinutes <= 0) return "Invalid interval";

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startTimeMillis);

            // Loop forward until we get a future time
            Calendar now = Calendar.getInstance();
            while (!cal.after(now)) {
                cal.add(Calendar.MINUTE, intervalMinutes);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(cal.getTime());
        } catch (NumberFormatException e) {
            return "Invalid interval";
        }
    }
}
