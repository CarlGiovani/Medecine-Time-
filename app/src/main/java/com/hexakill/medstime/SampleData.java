package com.hexakill.medstime;

import java.util.ArrayList;
import java.util.List;

public class SampleData {

    // Sample reminders (already existing)
    public static List<Reminder> getSampleReminders() {
        List<Reminder> reminders = new ArrayList<>();
        reminders.add(new Reminder("08:00 AM", "Only once"));
        reminders.add(new Reminder("01:00 PM", "Daily"));
        reminders.add(new Reminder("09:00 PM", "Daily"));
        return reminders;
    }

    // New: Sample medicines
    public static List<Medicine> getSampleMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        medicines.add(new Medicine("Paracetamol", "500mg", "After meals"));
        medicines.add(new Medicine("Ibuprofen", "200mg", "Before meals"));
        medicines.add(new Medicine("Vitamin C", "1000mg", "Once a day"));
        return medicines;
    }
}
