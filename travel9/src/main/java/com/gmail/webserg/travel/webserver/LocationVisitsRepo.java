package com.gmail.webserg.travel.webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.Visit;
import com.networknt.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;

import static java.nio.file.StandardOpenOption.*;
import static java.util.stream.Collectors.groupingBy;


public class LocationVisitsRepo {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    void load(List<Location> locations, List<Visit> visits) throws Exception {
        getPath().toFile().createNewFile();
        ObjectMapper mapper = new ObjectMapper();
        Set<OpenOption> options = new HashSet<>();
        options.add(WRITE);
        try (FileChannel fc = (FileChannel.open(getPath(), options))) {
            fc.position(0);

            Map<Integer, List<Visit>> locationVisits =
                    visits.stream().skip(1).collect(
                            groupingBy(Visit::getLocation)
                    );

            for (Location location : locations) {
                if (location == null) continue;
                List<Visit> visitsList = locationVisits.get(location.getId());
                if (visitsList != null) {
                    byte data[] = mapper.writeValueAsBytes(visitsList);
                    location.setVisitsPosition(fc.position());
                    location.setVisitsSize(data.length);
                    ByteBuffer out = ByteBuffer.wrap(data);
                    while (out.hasRemaining()) {
                        fc.write(out);
                    }
                }
            }
        }
    }


    private byte[] readLocationVisits(Location location) {
        Set<OpenOption> options = new HashSet<>();
        options.add(READ);
        ByteBuffer copy = ByteBuffer.allocate(location.getVisitsSize());
        try (FileChannel fc = (FileChannel.open(getPath(), options))) {
            fc.position(location.getVisitsPosition());
            int nread;

            do {
                nread = fc.read(copy);
            } while (nread != -1 && copy.hasRemaining());
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return copy.array();
    }

    boolean appendLocationVisits(Location location, List<Visit> visits) {
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
            synchronized (location) {
                location.setVisitsPosition(position);
                location.setVisitsSize(size);
            }
            try {
                while (out.hasRemaining())
                    fc.write(out);
            } finally {
//                lock.release();
            }
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
            return false;
        }
        return true;
    }


    private Path getPath() {
        return Paths.get(TravelConfig.PATH + "/locationVisits.data");
    }

    List<Visit> get(Location location) {
        ObjectMapper mapper = new ObjectMapper();
        if (location.getVisitsSize() == 0) return new ArrayList<>();
        byte[] data = readLocationVisits(location);
        try {
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionLikeType(List.class, Visit.class));
        } catch (Throwable e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}
