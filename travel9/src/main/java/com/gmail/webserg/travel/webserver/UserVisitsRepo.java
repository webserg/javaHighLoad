package com.gmail.webserg.travel.webserver;

import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.handler.Utils;
import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;


public class UserVisitsRepo {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Map<Integer, List<Integer>> userVisits = new ConcurrentHashMap<>();

    void load(List<Visit> visits) throws Exception {
        visits.stream().skip(1).collect(
                groupingBy(Visit::getUser)
        ).forEach((k, v) -> userVisits.put(k, v.stream().map(Visit::getId).collect(Collectors.toList())));
    }

    void appendUserVisits(Visit oldVisit, Visit newVisit) {
        userVisits.getOrDefault(oldVisit.getUser(), Collections.EMPTY_LIST).removeIf(id -> id.equals(oldVisit.getId()));
        add(newVisit);
    }

    List<Integer> get(User user) {
        return userVisits.getOrDefault(user.getId(), Collections.EMPTY_LIST);
    }

    void add(Visit newVisit) {
        List<Integer> visits = userVisits.get(newVisit.getUser());
        if (visits == null) {
            List<Integer> newVisits = new ArrayList<>(10);
            newVisits.add(newVisit.getId());
            userVisits.put(newVisit.getUser(), newVisits);
        } else {
            visits.add(newVisit.getId());
        }
    }

    LocalDateTime readTime() {
        try (Stream<String> stream = Files.lines(Paths.get(Utils.OPTIONS_PATH + "/options.txt"))) {
            List<String> res = stream.collect(Collectors.toList());
            return LocalDateTime.ofEpochSecond(Long.parseLong(res.get(0)), 0, ZoneOffset.UTC);
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return LocalDateTime.now(ZoneOffset.UTC);
        }
    }
}
