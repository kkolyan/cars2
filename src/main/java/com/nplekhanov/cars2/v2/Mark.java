package com.nplekhanov.cars2.v2;

/**
 * @author nplekhanov
 */
public class Mark {
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
}
