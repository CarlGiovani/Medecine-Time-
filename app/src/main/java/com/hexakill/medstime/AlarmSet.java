package com.hexakill.medstime;

import java.util.Calendar;
import java.util.Objects;

public class AlarmSet {

    private int id;
    private String medicineName;
    private String medicineDescription = "";
    private String alarmInterval;        // ALWAYS stored as STRING
    private String alarmNote;
    private long startTime;
    private long nextAlarmTime;
    private boolean active = true;
    private boolean userCreated = false;

    // ==========================
    // Constructors
    // ==========================
    public AlarmSet(int id, String medicineName, String interval, String note, long startTime) {
        this.id = id;
        this.medicineName = medicineName;
        this.alarmInterval = interval;
        this.alarmNote = note;
        this.startTime = startTime;
    }

    public AlarmSet(int id, String medicineName, String description, String interval,
                    String note, long startTime) {
        this.id = id;
        this.medicineName = medicineName;
        this.medicineDescription = description;
        this.alarmInterval = interval;
        this.alarmNote = note;
        this.startTime = startTime;
    }

    // ==========================
    // Getters & Setters
    // ==========================
    public int getId() { return id; }
    public String getMedicineName() { return medicineName; }
    public String getMedicineDescription() { return medicineDescription; }
    public String getAlarmInterval() { return alarmInterval; }
    public String getAlarmNote() { return alarmNote; }
    public long getStartTime() { return startTime; }
    public long getNextAlarmTime() { return nextAlarmTime; }

    public boolean isActive() { return active; }
    public boolean isUserCreated() { return userCreated; }

    public void setActive(boolean active) { this.active = active; }
    public void setUserCreated(boolean userCreated) { this.userCreated = userCreated; }
    public void setMedicineDescription(String description) { this.medicineDescription = description; }

    // ==========================
    // COMPUTE NEXT ALARM TIME
    // ==========================
    public void computeNextAlarmTime() {
        long now = System.currentTimeMillis();

        try {
            int intervalHours = Integer.parseInt(alarmInterval);
            long intervalMillis = intervalHours * 60L * 60L * 1000L;

            long next = startTime;

            // If the start time is in the past, advance until ahead of now
            while (next < now) {
                next += intervalMillis;
            }

            nextAlarmTime = next;

        } catch (Exception e) {
            // Fallback to "now + 1 hour"
            nextAlarmTime = now + (60L * 60L * 1000L);
        }
    }

    // ==========================
    // DiffUtil Support
    // ==========================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlarmSet)) return false;
        AlarmSet alarm = (AlarmSet) o;
        return id == alarm.id &&
                startTime == alarm.startTime &&
                nextAlarmTime == alarm.nextAlarmTime &&
                active == alarm.active &&
                Objects.equals(medicineName, alarm.medicineName) &&
                Objects.equals(medicineDescription, alarm.medicineDescription) &&
                Objects.equals(alarmInterval, alarm.alarmInterval) &&
                Objects.equals(alarmNote, alarm.alarmNote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, medicineName, medicineDescription, alarmInterval, alarmNote,
                startTime, nextAlarmTime, active);
    }
}
