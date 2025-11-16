package com.hexakill.medstime;

public class Medicine {
    private String name;
    private String dose;
    private String instructions;

    public Medicine(String name, String dose, String instructions) {
        this.name = name;
        this.dose = dose;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public String getDose() {
        return dose;
    }

    public String getInstructions() {
        return instructions;
    }
}
