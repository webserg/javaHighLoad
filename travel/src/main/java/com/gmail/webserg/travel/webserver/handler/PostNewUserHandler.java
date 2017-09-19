package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.UserPostQueryParam;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostNewUserHandler implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            UserPostQueryParam q = getRequest(exch.getQueryParameters());
            if (q.id == null || q.first_name ==null || q.last_name == null || q.birth_date == null || q.gender == null
                    || q.gender.length() > 1 || q.email == null || DataBase.getDb().getUser(q.id).isPresent()) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send("{}");
            DataBase.getDb().addUser(q);

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    public static UserPostQueryParam getRequest(Map q) {
        Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
        Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("first_name"));
        String first_name = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("last_name"));
        String last_name = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("gender"));
        String gender = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("birth_date"));
        Long birth_day = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("email"));
        String email = tmp.orElse(null);
        return new UserPostQueryParam(id, first_name, last_name, birth_day, gender, email);
    }

}
