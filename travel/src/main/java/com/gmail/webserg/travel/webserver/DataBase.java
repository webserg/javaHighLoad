package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.handler.UserVisitsRequest;
import com.gmail.webserg.travel.webserver.handler.UserVisitsResponse;
import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DataBase {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private List<User> users;
    private List<Location> locations;
    private List<Visit> visits;
    private static final DataBase db = new DataBase();
    private final UserVisitsRepo userVisitsRepo = new UserVisitsRepo();

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

    public Optional<List<UserVisitsResponse>> getUserVisits(int id, UserVisitsRequest req) {

        return Optional.of(users.get(id)).flatMap(userVisitsRepo::get);
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


//    public List<UserVisitsResponse> getUserVisitsSize(int id, UserVisitsRequest req) {
//        List<UserVisitsResponse> list = new ArrayList<>();
//        list.add(new UserVisitsResponse(2, 958656902, "Кольский полуостров"));
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
            userVisitsRepo.load(users, locations, visits);
            System.gc();
        } catch (Exception e) {
            logger.error("userVisitsRepo wasn't loaded", e);
        }
    }
}
