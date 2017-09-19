package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.LocationPostQueryParam;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostNewLocationHandler implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            LocationPostQueryParam q = getRequest(exch.getQueryParameters());
            if (q.id == null || q.place == null || q.country == null || q.city == null || q.distance == null
                    || DataBase.getDb().getLocation(q.id).isPresent()) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send("{}");
            DataBase.getDb().addLocation(q);

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    public static LocationPostQueryParam getRequest(Map q) {
        Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
        Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("place"));
        String place = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("country"));
        String country = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("city"));
        String city = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("distance"));
        int distance = tmp.map(Integer::parseInt).orElse(null);
        return new LocationPostQueryParam(id, place, country, city, distance);
    }

}
