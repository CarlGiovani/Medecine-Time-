package com.hexakill.medstime;

import java.util.ArrayList;
import java.util.List;

public class AlarmSampleData {

    public static List<AlarmSet> getAlarmSets() {
        List<AlarmSet> list = new ArrayList<>();

        list.add(new AlarmSet(
                "Paracetamol",
                "For fever and mild pain",
                "Daily",
                "Every 8 hours",
                "Take after meals"
        ));

        list.add(new AlarmSet(
                "Vitamin C",
                null,
                "Only Once",
                "None",
                "Drink water after"
        ));

        list.add(new AlarmSet(
                "Ibuprofen",
                "Anti-inflammatory",
                "Daily",
                "Every 12 hours",
                "Take only if needed"
        ));

        return list;
    }
}
