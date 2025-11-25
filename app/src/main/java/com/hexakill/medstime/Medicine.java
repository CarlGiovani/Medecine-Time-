package com.hexakill.medstime;

public class Medicine {
    private String name;
    private String description; // optional if you want descriptions

    public Medicine(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
