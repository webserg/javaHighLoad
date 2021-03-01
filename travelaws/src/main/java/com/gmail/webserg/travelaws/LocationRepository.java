package com.gmail.webserg.travelaws;

import com.gmail.webserg.travelaws.domain.Location;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {
    public Location findOne(int id) {
        return new Location(id, "place", "us", "new york", 1000);
    }

}
