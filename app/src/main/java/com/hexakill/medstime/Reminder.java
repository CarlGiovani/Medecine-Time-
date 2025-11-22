package com.hexakill.medstime;

import java.io.Serializable;

public class Reminder implements Serializable {
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
