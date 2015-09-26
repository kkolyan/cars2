package com.nplekhanov.cars2.v2;

/**
 * @author nplekhanov
 */
public final class Bookmark {
    private String mark;
    private int year;
    private int page;

    public Bookmark(String mark, int year, int page) {
        this.mark = mark;
        this.year = year;
        this.page = page;
    }

    public String getMark() {
        return mark;
    }

    public int getYear() {
        return year;
    }

    public int getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;

        if (page != bookmark.page) return false;
        if (year != bookmark.year) return false;
        if (mark != null ? !mark.equals(bookmark.mark) : bookmark.mark != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mark != null ? mark.hashCode() : 0;
        result = 31 * result + year;
        result = 31 * result + page;
        return result;
    }
}
