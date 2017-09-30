package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.Visit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


public class LocationVisitsRepo {
    private final Map<Integer, Set<Integer>> locVisits = new HashMap<>();

    void load(List<Visit> visits) throws Exception {
        visits.stream().skip(1).collect(groupingBy(Visit::getLocation))
                .forEach((k, v) ->
                        locVisits.put(k, v.stream().map(Visit::getId).collect(Collectors.toCollection(TreeSet::new))));
    }

    @SuppressWarnings("rawtypes")
    void appendLocationVisits(Visit oldVisit, Visit newVisit) {
        locVisits.getOrDefault(oldVisit.getLocation(), Collections.EMPTY_SET).remove(oldVisit.getId());
        add(newVisit);
    }

    Set<Integer> get(Location location) {
        return locVisits.getOrDefault(location.getId(), Collections.EMPTY_SET);
    }

    void add(Visit newVisit) {
        Set<Integer> visits = locVisits.get(newVisit.getLocation());
        if (visits == null) {
            Set<Integer> newVisits = new TreeSet<>();
            newVisits.add(newVisit.getId());
            locVisits.put(newVisit.getLocation(), newVisits);
        } else {
            visits.add(newVisit.getId());
        }
    }
}
