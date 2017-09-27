package com.gmail.webserg.travel.webserver.params;

public class VisitPostQueryParam {
    public  Integer id;
    public  Integer location;
    public  Integer user;
    public  Long visited_at;
    public  Integer mark;

    public VisitPostQueryParam() {
    }

    public VisitPostQueryParam(Integer id, Integer location, Integer user, Long visited_at, Integer mark) {
        this.id = id;
        this.location = location;
        this.user = user;
        this.visited_at = visited_at;
        this.mark = mark;
    }

    public boolean notNewValid() {
        return id == null || location == null || user == null || visited_at == null || mark == null;

    }
    public boolean notUpdateValid() {
        return id != null;

    }
}
