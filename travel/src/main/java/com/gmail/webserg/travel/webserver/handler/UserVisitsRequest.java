package com.gmail.webserg.travel.webserver.handler;

public final class UserVisitsRequest   {
    final Integer id ;
    final Long toDate ;
    final Long fromDate ;
    final String country ;
    final Integer toDistance ;

    public UserVisitsRequest(Integer id, Long toDate, Long fromDate, String country, Integer toDistance) {
        this.id = id;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.country = country;
        this.toDistance = toDistance;
    }
}
