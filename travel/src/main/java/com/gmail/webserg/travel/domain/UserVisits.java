package com.gmail.webserg.travel.domain;

public class UserVisits {
    int mark;
    long visited_at;
    String place = "";

    public UserVisits() {
    }

    public UserVisits(int mark, long visited_at, String place) {
        this.mark = mark;
        this.visited_at = visited_at;
        this.place = place;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public long getVisited_at() {
        return visited_at;
    }

    public void setVisited_at(long visited_at) {
        this.visited_at = visited_at;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public String toString() {
        return "UserVisits{" +
                "mark=" + mark +
                ", visited_at=" + visited_at +
                ", place='" + place + '\'' +
                '}';
    }
}
