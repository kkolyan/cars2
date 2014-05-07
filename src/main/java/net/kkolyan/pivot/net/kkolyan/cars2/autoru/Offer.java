package net.kkolyan.pivot.net.kkolyan.cars2.autoru;

import java.util.Date;

/**
 * @author nplekhanov
 */
public class Offer {

    private String mark;
    private String model;
    private String url;
    private int price;
    private int year;
    private String engine;
    private String type;
    private boolean rightHand;
    private int running;
    private boolean photo;
    private String color;
    private String city;
    private String availability;
    private Date parsedAt;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRightHand() {
        return rightHand;
    }

    public void setRightHand(boolean rightHand) {
        this.rightHand = rightHand;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public boolean isPhoto() {
        return photo;
    }

    public void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Date getParsedAt() {
        return parsedAt;
    }

    public void setParsedAt(Date parsedAt) {
        this.parsedAt = parsedAt;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "mark='" + mark + '\'' +
                ", model='" + model + '\'' +
                ", url='" + url + '\'' +
                ", price=" + price +
                ", year=" + year +
                ", engine='" + engine + '\'' +
                ", type='" + type + '\'' +
                ", rightHand='" + rightHand + '\'' +
                ", running=" + running +
                ", photo=" + photo +
                ", color='" + color + '\'' +
                ", city='" + city + '\'' +
                ", availability='" + availability + '\'' +
                ", parsedAt=" + parsedAt +
                '}';
    }
}
