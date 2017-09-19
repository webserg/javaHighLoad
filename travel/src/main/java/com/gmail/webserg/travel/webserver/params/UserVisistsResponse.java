package com.gmail.webserg.travel.webserver.params;

import com.gmail.webserg.travel.domain.UserVisits;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserVisistsResponse {
    List<UserVisits> visits;

    public UserVisistsResponse(List<UserVisits> visits) {
        this.visits = visits.stream().sorted(
                Comparator.comparingLong(UserVisits::getVisited_at))
        .collect(Collectors.toList());
    }

    public List<UserVisits> getVisits() {
        return visits;
    }

    public void setVisits(List<UserVisits> visits) {
        this.visits = visits;
    }
}
