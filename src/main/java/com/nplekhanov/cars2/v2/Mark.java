package com.nplekhanov.cars2.v2;

/**
 * @author nplekhanov
 */
public class Mark implements MeaningfulTextProvider {
    private String markId;
    private String markTitle;

    public Mark(String markId, String markTitle) {
        this.markId = markId;
        this.markTitle = markTitle;
    }

    public String getMarkId() {
        return markId;
    }

    public String getMarkTitle() {
        return markTitle;
    }

    @Override
    public String toString() {
        return "Mark{" +
                "markId='" + markId + '\'' +
                ", markTitle='" + markTitle + '\'' +
                '}';
    }

    @Override
    public String asMeaningFulText() {
        return markTitle;
    }
}
