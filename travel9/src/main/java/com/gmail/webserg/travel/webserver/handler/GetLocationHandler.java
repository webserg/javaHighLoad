package com.gmail.webserg.travel.webserver.handler;

import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.webserver.DataBase;

import java.util.Optional;

public class GetLocationHandler extends GetEntityIdHandler {
    @Override
    public Optional<Location> getEntity(int id) {
        return DataBase.getDb().getLocation(id);
    }
}

