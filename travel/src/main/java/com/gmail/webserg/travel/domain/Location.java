package com.gmail.webserg.travel.domain;

public class Location {
    int id;
    String place;
    String country;
    String city;
    int distance;
    transient long visitsPosition;
    transient int visitsSize;

    public Location(int id, String place, String country, String city, int distance) {
        this.id = id;
        this.place = place;
        this.country = country;
        this.city = city;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setVisitsPosition(long visitsPosition) {
        this.visitsPosition = visitsPosition;
    }

    public void setVisitsSize(int visitsSize) {
        this.visitsSize = visitsSize;
    }

    public long getVisitsPosition() {
        return visitsPosition;
    }

    public int getVisitsSize() {
        return visitsSize;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", distance=" + distance +
                '}';
    }

    public synchronized void update(String country, String city, String place, Integer distance) {
        if(country != null) this.country = country;
        if(city != null) this.city = city;
        if(place != null) this.place = place;
        if(distance != null) this.distance = distance;
    }
}
