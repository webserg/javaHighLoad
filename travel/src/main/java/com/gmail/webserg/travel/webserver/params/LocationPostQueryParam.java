package com.gmail.webserg.travel.webserver.params;

public class LocationPostQueryParam {
    public final Integer id;
    public final String place;
    public final String country;
    public final String city;
    public final Integer distance;

    public LocationPostQueryParam(Integer id, String place, String country, String city, Integer distance) {
        this.id = id;
        this.place = place;
        this.country = country;
        this.city = city;
        this.distance = distance;
    }
}
