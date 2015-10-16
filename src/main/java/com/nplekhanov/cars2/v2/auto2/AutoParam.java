package com.nplekhanov.cars2.v2.auto2;

import com.nplekhanov.cars2.v2.Mark;
import com.nplekhanov.cars2.v2.MeaningfulTextProvider;

/**
 * @author nplekhanov
 */
public class AutoParam implements MeaningfulTextProvider {
    private Mark mark;
    private int year;

    public AutoParam(Mark mark, int year) {
        this.mark = mark;
        this.year = year;
    }

    public Mark getMark() {
        return mark;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "AutoParam{" +
                "mark=" + mark +
                ", year=" + year +
                '}';
    }

    @Override
    public String asMeaningFulText() {
        return mark.getMarkTitle()+"/"+year;
    }
}
