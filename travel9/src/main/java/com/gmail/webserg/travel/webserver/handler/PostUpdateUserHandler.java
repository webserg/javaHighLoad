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

                    User newUser = validation(exch, data);
                    if (newUser == null) return;
                    exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                    exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, Utils.CHARSET);
                    exch.getResponseSender().send(Utils.POST_ANSWER.duplicate());
                    DataBase.getDb().updateUser(user.get(), newUser);
                } catch (Throwable ex) {
                    exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                }
            });
        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    private User validation(HttpServerExchange exch, byte[] data) throws java.io.IOException {
        Map<String, String> map = mapper.readValue(data, new TypeReference<Map
                <String, String>>() {
        });
        if (map.values().contains(null)) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
            return null;
        }
        User newUser = new User();
        if (map.get("first_name") != null) {
            newUser.setFirst_name(map.get("first_name"));
        }
        if (map.get("last_name") != null) {
            newUser.setLast_name(map.get("last_name"));
        }
        if (map.get("birth_day") != null) {
            newUser.setBirth_date(Long.parseLong(map.get("birth_day")));
        }
        if (map.get("gender") != null) {
            String g = map.get("gender");
            if (g.length() > 1) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return null;
            }
            newUser.setGender(g);
        }
        if (map.get("email") != null) {
            newUser.setEmail(map.get("email"));
        }

        return newUser;
    }
}