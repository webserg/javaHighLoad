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

public class PostUpdateUserHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) exch.getPathParameters().get("id"));
            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
            UserPostQueryParam q = PostNewUserHandler.getRequest(exch.getQueryParameters());
            if (id == null || q.id != null || (q.gender != null &&  q.gender.length() > 1) ) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }
            Optional<User> user = DataBase.getDb().getUser(id);
            if(!user.isPresent()){
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send("{}");
            DataBase.getDb().updateUser(user.get(), q);

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }


}
