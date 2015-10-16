package com.nplekhanov.cars2.v2.auto2;

/**
 * @author nplekhanov
 */
public class SummaryItem {

    private String title;
    private int count;
    private String url;

    public SummaryItem(String url, String title, int count) {
        this.url = url;
        this.title = title;
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", count=" + count +
                ", url='" + url + '\'' +
                '}';
    }
}
