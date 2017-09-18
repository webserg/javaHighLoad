package com.gmail.webserg.travel.webserver.handler;

public final class UserVisitsRequest   {
    public final Integer id ;
    public final Long toDate ;
    public final Long fromDate ;
    public final String country ;
    public final Integer toDistance ;

    public UserVisitsRequest(Integer id, Long toDate, Long fromDate, String country, Integer toDistance) {
        this.id = id;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.country = country;
        this.toDistance = toDistance;
    }
}
