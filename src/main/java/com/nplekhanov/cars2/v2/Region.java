package com.nplekhanov.cars2.v2;

/**
 * @author nplekhanov
 */
public class Region {
    private long id;
    private String name;
    private long offers;

    public Region(long id, String name, long offers) {
        this.id = id;
        this.name = name;
        this.offers = offers;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getOffers() {
        return offers;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

