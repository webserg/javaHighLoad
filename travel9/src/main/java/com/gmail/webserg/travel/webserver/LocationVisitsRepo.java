package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.Visit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


public class LocationVisitsRepo {
    private final Map<Integer, List<Integer>> locVisits = new ConcurrentHashMap<>();

    void load(List<Visit> visits) throws Exception {
        visits.stream().skip(1).collect(
                groupingBy(Visit::getLocation)
        ).forEach((k, v) -> locVisits.put(k, v.stream().map(Visit::getId).collect(Collectors.toList())));
    }

    void appendLocationVisits(Visit oldVisit, Visit newVisit) {
        locVisits.getOrDefault(oldVisit.getLocation(), new ArrayList<>()).removeIf(id -> id.equals(oldVisit.getId()));
        locVisits.getOrDefault(newVisit.getLocation(), new ArrayList<>()).add(newVisit.getId());
    }

    List<Integer> get(Location location) {
        return locVisits.getOrDefault(location.getId(), new ArrayList<>());
    }

    void add(Location location, Visit newVisit) {
        locVisits.getOrDefault(location.getId(), new ArrayList<>()).add(newVisit.getId());
    }
}
