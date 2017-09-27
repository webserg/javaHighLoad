package com.gmail.webserg.travel.webserver.handler;

import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;

import java.util.Optional;

public class GetUserHandler extends GetEntityIdHandler {
    @Override
    public Optional<User> getEntity(int id) {
        return DataBase.getDb().getUser(id);
    }
}

