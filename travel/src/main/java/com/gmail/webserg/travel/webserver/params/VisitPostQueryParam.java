package com.gmail.webserg.travel.webserver.params;

public class VisitPostQueryParam {
    public final Integer id;
    public final Integer location;
    public final Integer user;
    public final Long visited_at;
    public final Integer mark;

    public VisitPostQueryParam(Integer id, Integer location, Integer user, Long visited_at, Integer mark) {
        this.id = id;
        this.location = location;
        this.user = user;
        this.visited_at = visited_at;
        this.mark = mark;
    }
}
