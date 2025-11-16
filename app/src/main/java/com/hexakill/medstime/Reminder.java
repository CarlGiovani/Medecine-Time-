package com.hexakill.medstime;

public class Reminder {
    private String name;
    private String type;

    public Reminder(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
