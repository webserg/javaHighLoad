package com.gmail.webserg.travel.domain;

public class Location {
    Integer id;
    String place;
    String country;
    String city;
    Integer distance;
    transient long visitsPosition;
    transient int visitsSize;

    public Location() {
    }

    public Location(Integer id, String place, String country, String city, Integer distance) {
        this.id = id;
        this.place = place;
        this.country = country;
        this.city = city;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != location.id) return false;
        if (distance != location.distance) return false;
        if (visitsPosition != location.visitsPosition) return false;
        if (visitsSize != location.visitsSize) return false;
        if (place != null ? !place.equals(location.place) : location.place != null) return false;
        if (country != null ? !country.equals(location.country) : location.country != null) return false;
        return city != null ? city.equals(location.city) : location.city == null;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
