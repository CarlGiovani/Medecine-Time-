package com.hexakill.medstime;

import java.util.Objects;

public class Medicine {

    private int id;
    private String name;
    private String description;

    // Optional fields if you want interval + note later
    private String interval;
    private String note;

    // ---------------------------------------------------------
    // Constructors
    // ---------------------------------------------------------

    // Minimum constructor (used for prebuilt list)
    public Medicine(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.interval = "";
        this.note = "";
    }

    // Constructor without ID (if needed)
    public Medicine(String name, String description) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.interval = "";
        this.note = "";
    }

    // Full constructor (if needed for future expansion)
    public Medicine(int id, String name, String description, String interval, String note) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.interval = interval;
        this.note = note;
    }

    // ---------------------------------------------------------
    // Getters / Setters
    // ---------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getInterval() { return interval; }

    public void setInterval(String interval) { this.interval = interval; }

    public String getNote() { return note; }

    public void setNote(String note) { this.note = note; }

    // ---------------------------------------------------------
    // For DiffUtil (required!)
    // ---------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Medicine)) return false;

        Medicine medicine = (Medicine) o;

        return id == medicine.id &&
                Objects.equals(name, medicine.name) &&
                Objects.equals(description, medicine.description) &&
                Objects.equals(interval, medicine.interval) &&
                Objects.equals(note, medicine.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, interval, note);
    }
}
