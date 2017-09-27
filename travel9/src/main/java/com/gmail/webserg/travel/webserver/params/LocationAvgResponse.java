package com.gmail.webserg.travel.webserver.params;

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
