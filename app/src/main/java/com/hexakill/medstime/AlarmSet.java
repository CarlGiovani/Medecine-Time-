package com.hexakill.medstime;

public class AlarmSet {
    private String medicineName;
    private String medicineDescription;
    private String alarmType;
    private String alarmInterval;
    private String alarmNote;

    public AlarmSet(String medicineName, String medicineDescription, String alarmType, String alarmInterval, String alarmNote) {
        this.medicineName = medicineName;
        this.medicineDescription = medicineDescription;
        this.alarmType = alarmType;
        this.alarmInterval = alarmInterval;
        this.alarmNote = alarmNote;
    }

    public String getMedicineName() { return medicineName; }
    public String getMedicineDescription() { return medicineDescription; }
    public String getAlarmType() { return alarmType; }
    public String getAlarmInterval() { return alarmInterval; }
    public String getAlarmNote() { return alarmNote; }
}
