package com.gmail.webserg.travel.domain;

public class Visit {
    int id;
    int location;
    int user;
    long visited_at;
    int mark;

    public Visit() {
    }

    public Visit(int id, int location, int user, long visited_at, int mark) {
        this.id = id;
        this.location = location;
        this.user = user;
        this.visited_at = visited_at;
        this.mark = mark;
    }

    public void update(Integer location, Integer user, Long visited_at, Integer mark) {
        if (location != null) this.location = location;
        if (user != null) this.user = user;
        if (visited_at != null) this.visited_at = visited_at;
        if (mark != null) this.mark = mark;
    }

    public Visit copy() {
        return new Visit(id, location, user, visited_at, mark);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getVisited_at() {
        return visited_at;
    }

    public void setVisited_at(long visited_at) {
        this.visited_at = visited_at;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visit visit = (Visit) o;

        return id != visit.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        return result;
    }
}
