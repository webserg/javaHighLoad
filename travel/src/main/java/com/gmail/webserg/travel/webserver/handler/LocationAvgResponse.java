package com.gmail.webserg.travel.webserver.handler;

public class LocationAvgResponse {
    private Double avg;

    public LocationAvgResponse(Double avg) {
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }
}
