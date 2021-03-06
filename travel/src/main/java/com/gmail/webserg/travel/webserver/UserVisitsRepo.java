package com.gmail.webserg.travel.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.handler.Utils;
import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.Collectors.groupingBy;


public class UserVisitsRepo {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    void load(List<User> users, List<Visit> visits) throws Exception {
        getPath().toFile().createNewFile();
        ObjectMapper mapper = new ObjectMapper();
        Set<OpenOption> options = new HashSet<>();
        options.add(WRITE);
        try (FileChannel fc = (FileChannel.open(getPath(), options))) {
            fc.position(0);

            Map<Integer, List<Visit>> userVisits =
                    visits.stream().skip(1).collect(
                            groupingBy(Visit::getUser)
                    );

            for (User user : users) {
                if (user == null) continue;
                List<Visit> visitsList = userVisits.get(user.getId());
                if (visitsList != null) {
                    byte data[] = mapper.writeValueAsBytes(visitsList);
                    user.setUserVisitsPosition(fc.position());
                    user.setUserVisitsSize(data.length);
                    ByteBuffer out = ByteBuffer.wrap(data);
                    while (out.hasRemaining()) {
                        fc.write(out);
                    }
                }
            }
        }
    }

    LocalDateTime readTime() {
        try (Stream<String> stream = Files.lines(Paths.get(Utils.optionsPath + "/options.txt"))) {
            List<String> res = stream.collect(Collectors.toList());
            return LocalDateTime.ofEpochSecond(Long.parseLong(res.get(0)), 0, ZoneOffset.UTC);
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return LocalDateTime.now(ZoneOffset.UTC);
        }
    }


    private byte[] readUserVisits(User user) {
        Set<OpenOption> options = new HashSet<>();
        options.add(READ);
        ByteBuffer copy = ByteBuffer.allocate(user.getUserVisitsSize());
        try (FileChannel fc = (FileChannel.open(getPath(), options))) {
            fc.position(user.getUserVisitsPosition());
            int nread;

            do {
                nread = fc.read(copy);
            } while (nread != -1 && copy.hasRemaining());
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return copy.array();
    }

    boolean appendUserVisits(User user, List<Visit> visits) {
        ObjectMapper mapper = new ObjectMapper();
        Set<OpenOption> options = new HashSet<>();
        options.add(WRITE);
        try (FileChannel fc = (FileChannel.open(getPath(), options))) {
            byte data[] = mapper.writeValueAsBytes(visits);
            int size = data.length;
            long position = fc.size();
            ByteBuffer out = ByteBuffer.wrap(data);
            fc.position(position);
//            FileLock lock = fc.lock(position, size, true);
            synchronized (user) {
                user.setUserVisitsPosition(position);
                user.setUserVisitsSize(size);
            }
            try {
                while (out.hasRemaining())
                    fc.write(out);
            } finally {
//                lock.release();
            }
        } catch (IOException x) {
            logger.error("I/O Exception: " + x);
            return false;
        }
        return true;
    }


    private Path getPath() {
        return Paths.get(TravelConfig.PATH + "/userVisits.data");
    }

    List<Visit> get(User user) {
        ObjectMapper mapper = new ObjectMapper();
        if (user.getUserVisitsSize() == 0) return new ArrayList<>();
        byte[] data = readUserVisits(user);
        try {
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionLikeType(List.class, Visit.class));
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
