package com.hexakill.medstime;

import java.util.ArrayList;
import java.util.List;

public class SampleMedicines {
    public static List<Medicine> getSampleMedicines() {
        List<Medicine> list = new ArrayList<>();
        list.add(new Medicine(
                "Paracetamol",
                "Used for fever and mild pain."));
        list.add(new Medicine(
                "Ibuprofen",
                "Reduces inflammation."));
        list.add(new Medicine(
                "Vitamin C",
                "Boosts immune system."));
        list.add(new Medicine(
                "Cetirizine",
                "Relieves allergy symptoms."));
        list.add(new Medicine(
                "Amoxicillin",
                "Antibiotic for infections."));
        return list;
    }
}
