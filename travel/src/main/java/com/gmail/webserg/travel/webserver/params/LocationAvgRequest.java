package com.gmail.webserg.travel.webserver.params;

public final class LocationAvgRequest {
    public final Integer id ;
    public final Long toDate ;
    public final Long fromDate ;
    public final Long toAge;
    public final Long fromAge;
    public final String gender;

    public LocationAvgRequest(Integer id, Long toDate, Long fromDate, Long toAge, Long fromAge, String gender) {
        this.id = id;
        this.toDate = toDate;
        this.fromDate = fromDate;
        this.toAge = toAge;
        this.fromAge = fromAge;
        this.gender = gender;
    }
}
