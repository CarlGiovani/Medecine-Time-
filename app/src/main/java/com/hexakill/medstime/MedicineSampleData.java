package com.hexakill.medstime;

import java.util.ArrayList;
import java.util.List;

public class MedicineSampleData {

    public static List<Medicine> getMedicines() {
        List<Medicine> list = new ArrayList<>();

        list.add(new Medicine(
                "Paracetamol",
                "For fever and mild pain"
        ));

        list.add(new Medicine(
                "Vitamin C",
                null // no description
        ));

        list.add(new Medicine(
                "Ibuprofen",
                "Anti-inflammatory painkiller"
        ));

        return list;
    }
}
