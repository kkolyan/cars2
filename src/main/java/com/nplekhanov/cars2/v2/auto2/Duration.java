package com.nplekhanov.cars2.v2.auto2;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author nplekhanov
 */
public class Duration {
    private long millis;

    public Duration(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }

    public long getSeconds() {
        return getMillis() / 1000;
    }

    public long getMinutes() {
        return getSeconds() / 60;
    }

    public long getHours() {
        return getMinutes() / 60;
    }

    public long getDays() {
        return getHours() / 24;
    }

    public long getWeeks() {
        return getDays() / 7;
    }

    public String format() {
        long[] components = {
                getWeeks(),
                getDays() % 7,
                getHours() % 24,
                getMinutes() % 60,
                getSeconds() % 60,
                getMillis() % 1000
        };
        String[] labels = {
                "w", "d", "h", "m", "s", "ms"
        };
        Collection<String> compTexts = new ArrayList<>();
        for (int i = 0; i < components.length; i ++) {
            if (components[i] > 0) {
                compTexts.add(components[i]+labels[i]);
            }
        }
        if (compTexts.isEmpty()) {
            return "0";
        }
        return StringUtils.join(compTexts, " ");
    }
}
