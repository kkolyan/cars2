package com.nplekhanov.cars2.v2.moto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nplekhanov.cars2.v2.ShortDescription;

/**
 * @author nplekhanov
 */
public class Offer implements ShortDescription {
    private String mark;
    private String title;
    private String url;
    private String type;
    private int price;
    private boolean imagePresent;
    private int year;
    private int run;
    private String color;
    private String region;
    private boolean customsPassed;
    private boolean available;
    private String exception;
    private String sourceHtml;

    @Override
    public String getException() {
        return exception;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setImagePresent(boolean imagePresent) {
        this.imagePresent = imagePresent;
    }

    public boolean isImagePresent() {
        return imagePresent;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getRun() {
        return run;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setCustomsPassed(boolean customsPassed) {
        this.customsPassed = customsPassed;
    }

    public boolean isCustomsPassed() {
        return customsPassed;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setSourceHtml(String sourceHtml) {
        this.sourceHtml = sourceHtml;
    }

    public String getSourceHtml() {
        return sourceHtml;
    }
}
