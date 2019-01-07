package com.kfoszcz.makaoscore.view;

import java.util.Locale;

public class FormatHelper {
    public static String integer(Locale locale, int value) {
        return String.format(locale, "%d", value);
    }

    public static String percent(Locale locale, float value) {
        return String.format(locale, "%d%%", Math.round(value * 100));
    }
}
