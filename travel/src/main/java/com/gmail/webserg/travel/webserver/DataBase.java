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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.YEARS;

public final class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private List<User> users;
    private List<Location> locations;
    private List<Visit> visits;
    private static final DataBase db = new DataBase();
    private final UserVisitsRepo userVisitsRepo = new UserVisitsRepo();
    private final LocationVisitsRepo locVisitsRepo = new LocationVisitsRepo();
    private LocalDateTime generationDateTime;

    public static DataBase getDb() {
        return db;
    }

    public Optional<User> getUser(int id) {
        try {
            return Optional.of(users.get(id));
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public List<UserVisits> getUserVisits(User id, UserVisitsRequest req) {
        List<Visit> result = userVisitsRepo.get(id);
        return result.stream().filter(v -> req.country == null || req.country.equals(locations.get(v.getLocation()).getCountry()))
                .filter(v -> req.fromDate == null || req.fromDate < v.getVisited_at())
                .filter(v -> req.toDate == null || req.toDate > v.getVisited_at())
                .filter(v -> req.toDistance == null || req.toDistance > locations.get(v.getLocation()).getDistance())
                .map(this::map).collect(Collectors.toList());

    }

    public double getLocAvgResult(Location location, LocationAvgRequest req) {
        List<Visit> userVisits = locVisitsRepo.get(location);
        if (userVisits.size() == 0) return 0.0;
        List<Integer> marks = userVisits.stream()
                .filter(v -> req.gender == null || req.gender.equalsIgnoreCase(users.get(v.getUser()).getGender()))
                .filter(v -> req.fromDate == null || v.getVisited_at() > req.fromDate)
                .filter(v -> req.toDate == null || v.getVisited_at() < req.toDate)
                .filter(v -> req.fromAge == null || getAge(users.get(v.getUser()).getBirth_date()) >= req.fromAge)
                .filter(v -> req.toAge == null || getAge(users.get(v.getUser()).getBirth_date()) < req.toAge)
                .map(Visit::getMark).collect(Collectors.toList());
        if (marks.size() == 0) return 0.0;
        double sum = 0.0;
        for (int m : marks) {
            sum += m;
        }
        double avgTmp = sum / marks.size();
        return (new BigDecimal(avgTmp).setScale(5, BigDecimal.ROUND_HALF_UP)).doubleValue();
    }


    long getAge(Long bd) {
        return LocalDateTime.ofEpochSecond(bd, 0, ZoneOffset.UTC).until(generationDateTime, YEARS);
    }


    private UserVisits map(Visit v) {
        String place = locations.get(v.getLocation()).getPlace();
        if (place == null) {
            System.out.println(locations.get(v.getLocation()));
        }
        return new UserVisits(v.getMark(), v.getVisited_at(), place);
    }

    public Optional<Location> getLocation(int id) {
        try {
            return Optional.of(locations.get(id));
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Visit> getVisit(int id) {
        try {
            return Optional.of(visits.get(id));
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
            logger.error("users wasn't loaded");
            users = new ArrayList<>();
        }
        try {
            locations = new DataLoader<>(Location[].class, "locations", 77000).load();
            logger.info("locations loaded size=" + locations.size());
            System.gc();
        } catch (IOException e) {
            logger.error("locations wasn't loaded");
            locations = new ArrayList<>();
        }
        try {
            visits = new DataLoader<>(Visit[].class, "visits", 1001240).load();
            System.gc();
        } catch (IOException e) {
            logger.error("visits wasn't loaded");
            visits = new ArrayList<>();
        }
        try {
            userVisitsRepo.load(users, visits);
            locVisitsRepo.load(locations, visits);
            generationDateTime = userVisitsRepo.readTime();
            System.gc();
        } catch (Exception e) {
            logger.error("userVisitsRepo wasn't loaded", e);
        }
    }

    public void addUser(UserPostQueryParam q) {
        User newUser = new User(q.id, q.first_name, q.last_name, q.birth_date, q.gender, q.email);
        synchronized (users) {
            users.add(q.id, newUser);
        }
    }

    public void addLocation(LocationPostQueryParam q) {
        Location newLocation = new Location(q.id, q.country, q.city, q.place, q.distance);
        synchronized (locations) {
            locations.add(q.id, newLocation);
        }
    }

    public void addVisit(VisitPostQueryParam q) {
        Visit newVisit = new Visit(q.id, q.location, q.user, q.visited_at, q.mark);
        synchronized (visits) {
            visits.add(q.id, newVisit);
        }
        User user = users.get(newVisit.getUser());
        List<Visit> userVisits = userVisitsRepo.get(user);
        userVisits.add(newVisit);
        userVisitsRepo.appendUserVisits(user, userVisits);
        Location location = locations.get(newVisit.getLocation());
        List<Visit> locationVisits = locVisitsRepo.get(location);
        locationVisits.add(newVisit);
        locVisitsRepo.appendLocationVisits(location, locationVisits);
    }

    public void updateUser(final User user, UserPostQueryParam q) {
        user.update(q.first_name, q.last_name, q.birth_date, q.gender, q.email);
    }

    public void updateLocation(Location location, LocationPostQueryParam q) {
        location.update(q.country, q.city, q.place, q.distance);
    }

    public void updateVisit(Visit v, VisitPostQueryParam q) {
        Visit newVisit = new Visit(
                v.getId(),
                q.location == null ? q.location : v.getLocation(),
                q.user == null ? q.user : v.getUser(),
                q.visited_at == null ? q.visited_at : v.getVisited_at(),
                q.mark == null ? q.mark : v.getMark());

        if (v.equals(newVisit)) return;

        synchronized (visits) {
            visits.set(q.id, newVisit);
        }
        if (newVisit.getUser() == v.getUser()) {
            User user = users.get(v.getUser());
            List<Visit> userVisits = userVisitsRepo.get(user);
            userVisits.remove(v);
            userVisits.add(newVisit);
            userVisitsRepo.appendUserVisits(user, userVisits);
        } else {
            User user = users.get(v.getUser());
            List<Visit> userVisits = userVisitsRepo.get(user);
            userVisits.remove(v);
            userVisitsRepo.appendUserVisits(user, userVisits);

            User newUser = users.get(newVisit.getUser());
            List<Visit> newUserVisits = userVisitsRepo.get(user);
            newUserVisits.remove(v);
            userVisitsRepo.appendUserVisits(newUser, newUserVisits);
        }
//        Location location = locations.get(newVisit.getLocation());
//        List<Visit> locationVisits = locVisitsRepo.get(location);
//        locationVisits.add(newVisit);
//        locVisitsRepo.appendLocationVisits(location, locationVisits);
    }
}
