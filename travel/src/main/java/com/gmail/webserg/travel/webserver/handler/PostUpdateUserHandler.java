package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Optional;

public class PostUpdateUserHandler implements HttpHandler {
    private final ObjectMapper mapper = Config.getInstance().getMapper();


    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) exch.getPathParameters().get("id"));
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
                    User newUser = mapper.readValue(data, User.class);
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
}