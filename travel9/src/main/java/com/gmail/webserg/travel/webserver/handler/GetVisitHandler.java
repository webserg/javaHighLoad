package com.gmail.webserg.travel.webserver.handler;

import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.DataBase;

import java.util.Optional;

public class GetVisitHandler extends GetEntityIdHandler {
    @Override
    public Optional<Visit> getEntity(int id) {
        return DataBase.getDb().getVisit(id);
    }
}

