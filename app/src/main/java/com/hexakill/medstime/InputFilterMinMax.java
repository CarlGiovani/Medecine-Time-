package com.hexakill.medstime;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String input = dest.toString() + source.toString();
            int value = Integer.parseInt(input);
            if (value >= min && value <= max) {
                return null;
            }
        } catch (NumberFormatException e) { }
        return "";
    }
}
