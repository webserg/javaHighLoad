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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;


public class UserVisitsRepo {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Map<Integer, Set<Integer>> userVisits = new ConcurrentHashMap<>();

    void load(List<Visit> visits) throws Exception {
        visits.stream().skip(1).collect(
                groupingBy(Visit::getUser)
        ).forEach((k, v) -> userVisits.put(k, v.stream().map(Visit::getId).collect(Collectors.toCollection(TreeSet::new))));
    }

    void appendUserVisits(Visit oldVisit, Visit newVisit) {
        userVisits.getOrDefault(oldVisit.getUser(), Collections.EMPTY_SET).remove(oldVisit.getId());
        add(newVisit);
    }

    Set<Integer> get(User user) {
        return userVisits.getOrDefault(user.getId(), Collections.EMPTY_SET);
    }

    void add(Visit newVisit) {
        Set<Integer> visits = userVisits.get(newVisit.getUser());
        if (visits == null) {
            Set<Integer> newVisits = new TreeSet<>();
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
