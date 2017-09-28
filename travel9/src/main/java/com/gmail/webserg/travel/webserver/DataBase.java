package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.UserVisits;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.params.*;
import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.YEARS;

public final class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private List<User> users;
    private Map<Integer, User> newUsers = new ConcurrentHashMap<>();
    private List<Location> locations;
    private Map<Integer, Location> newLocations = new ConcurrentHashMap<>();
    private List<Visit> visits;
    private Map<Integer, Visit> newVisits = new ConcurrentHashMap<>();
    private static final DataBase db = new DataBase();
    private final UserVisitsRepo userVisitsRepo = new UserVisitsRepo();
    private final LocationVisitsRepo locVisitsRepo = new LocationVisitsRepo();
    private LocalDateTime generationDateTime;

    public static DataBase getDb() {
        return db;
    }

    public Optional<User> getUser(int id) {
        try {
            return Optional.of(users(id));
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    private User users(int id) {
        if (id < users.size())
            return users.get(id);
        return newUsers.get(id);
    }

    private Location locations(int id) {
        if (id < locations.size())
            return locations.get(id);
        return newLocations.get(id);
    }

    private Visit visits(int id) {
        if (id < visits.size())
            return visits.get(id);
        return newVisits.get(id);
    }

    public List<UserVisits> getUserVisits(User user, UserVisitsRequest req) {
        List<Visit> result = userVisitsRepo.get(user).stream().map(this::visits).collect(Collectors.toList());;
        return result.stream().filter(v -> req.country == null || req.country.equals(locations(v.getLocation()).getCountry()))
                .filter(v -> req.fromDate == null || req.fromDate < v.getVisited_at())
                .filter(v -> req.toDate == null || req.toDate > v.getVisited_at())
                .filter(v -> req.toDistance == null || req.toDistance > locations(v.getLocation()).getDistance())
                .map(this::map).collect(Collectors.toList());

    }

    public double getLocAvgResult(Location location, LocationAvgRequest req) {
        List<Visit> userVisits = locVisitsRepo.get(location).stream().map(this::visits).collect(Collectors.toList());
        if (userVisits.size() == 0) return 0.0;
        List<Integer> marks = userVisits.stream()
                .filter(v -> req.gender == null || req.gender.equalsIgnoreCase(users(v.getUser()).getGender()))
                .filter(v -> req.fromDate == null || v.getVisited_at() > req.fromDate)
                .filter(v -> req.toDate == null || v.getVisited_at() < req.toDate)
                .filter(v -> req.fromAge == null || getAge(users(v.getUser()).getBirth_date()) >= req.fromAge)
                .filter(v -> req.toAge == null || getAge(users(v.getUser()).getBirth_date()) < req.toAge)
                .map(Visit::getMark).collect(Collectors.toList());
        if (marks.size() == 0) return 0.0;
        double sum = 0.0;
        for (int m : marks) {
            sum += m;
        }
        double avgTmp = sum / marks.size();
        return (new BigDecimal(avgTmp).setScale(5, BigDecimal.ROUND_HALF_UP)).doubleValue();
    }


    private long getAge(Long bd) {
        return LocalDateTime.ofEpochSecond(bd, 0, ZoneOffset.UTC).until(generationDateTime, YEARS);
    }


    private UserVisits map(Visit v) {
        String place = locations(v.getLocation()).getPlace();
        return new UserVisits(v.getMark(), v.getVisited_at(), place);
    }

    public Optional<Location> getLocation(int id) {
        try {
            return Optional.of(locations(id));
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Visit> getVisit(int id) {
        try {
            return Optional.of(visits(id));
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }


//    public List<UserVisits> getUserVisitsSize(int id, UserVisitsRequest req) {
//        List<UserVisits> list = new ArrayList<>();
//        list.add(new UserVisits(2, 958656902, "Кольский полуостров"));
//        return list;
//    }

    private DataBase() {
    }

    public void loadData() {
        try {
            users = new DataLoader<>(User[].class, "users", 100123).load();
            logger.info("users loaded size=" + users.size());
            System.gc();
        } catch (IOException e) {
            logger.error("users wasn't loaded",e);
            users = new ArrayList<>();
        }
        try {
            locations = new DataLoader<>(Location[].class, "locations", 77000).load();
            logger.info("locations loaded size=" + locations.size());
            System.gc();
        } catch (IOException e) {
            logger.error("locations wasn't loaded",e);
            locations = new ArrayList<>();
        }
        try {
            visits = new DataLoader<>(Visit[].class, "visits", 1001240).load();
            logger.info("visits loaded size=" + visits.size());
            System.gc();
        } catch (IOException e) {
            logger.error("visits wasn't loaded",e);
            visits = new ArrayList<>();
        }
        try {
            userVisitsRepo.load(visits);
            locVisitsRepo.load(visits);
            generationDateTime = userVisitsRepo.readTime();
            System.gc();
        } catch (Exception e) {
            logger.error("userVisitsRepo wasn't loaded", e);
        }
    }

    public void addUser(User newUser) {
        newUsers.put(newUser.getId(), newUser);
    }

    public void addLocation(Location newLocation) {
        newLocations.put(newLocation.getId(), newLocation);
    }

    public void addVisit(VisitPostQueryParam q) {
        Visit newVisit = new Visit(q.id, q.location, q.user, q.visited_at, q.mark);
        newVisits.put(q.id, newVisit);
        locVisitsRepo.add(newVisit);
        userVisitsRepo.add(newVisit);
    }

    public void updateUser(final User user, User q) {
        user.update(q);
    }

    public void updateLocation(Location location, Location newLocation) {
        location.update(newLocation);
    }

    public void updateVisit(Visit oldVisit, VisitPostQueryParam queryParam) {
        Visit copyOldVisit = oldVisit.copy();
        oldVisit.update(queryParam.location, queryParam.user, queryParam.visited_at, queryParam.mark);
        if (queryParam.location != null) {
            locVisitsRepo.appendLocationVisits(copyOldVisit, oldVisit);
        }
        if (queryParam.user != null) {
            userVisitsRepo.appendUserVisits(copyOldVisit, oldVisit);
        }
    }
}
