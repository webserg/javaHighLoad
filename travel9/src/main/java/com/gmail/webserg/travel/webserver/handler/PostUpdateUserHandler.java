package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostUpdateUserHandler implements HttpHandler {
    private final ObjectMapper mapper = Config.getInstance().getMapper();


    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) exch.getQueryParameters().get("id"));
            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);

            if (id == null) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }

            Optional<User> user = DataBase.getDb().getUser(id);
            if (!user.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                try {
                    if (validation(exch, data)) return;
                    User newUser = mapper.readValue(data, User.class);
                    if (newUser.getGender() != null && newUser.getGender().length() > 1)
                        throw new IllegalArgumentException();
                    exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                    exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
                    exch.getResponseSender().send("{}");
                    DataBase.getDb().updateUser(user.get(), newUser);
                } catch (Throwable ex) {
                    exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                }
            });
        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    private boolean validation(HttpServerExchange exch, byte[] data) throws java.io.IOException {
        Map<String, Object> map = mapper.readValue(data, new TypeReference<Map
                <String, String>>() { });
        if (map.values().contains(null)) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
            return true;
        }
        return false;
    }
}