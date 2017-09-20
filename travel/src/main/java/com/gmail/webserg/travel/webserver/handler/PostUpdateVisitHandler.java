package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.VisitPostQueryParam;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostUpdateVisitHandler implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
//            Optional<String> tmp = Utils.toString((ArrayDeque<String>) exch.getPathParameters().get("id"));
//            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
//            VisitPostQueryParam q = getRequest(exch.getQueryParameters());
//            if (id == null || q.id != null ) {
//                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
//                return;
//            }
//            Optional<Visit> visit = DataBase.getDb().getVisit(id);
//            if(!visit.isPresent()){
//                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
//                return;
//            }
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send("{}");
//            DataBase.getDb().updateVisit(visit.get(), q);

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    private VisitPostQueryParam getRequest(Map q) {
        Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
        Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("location"));
        Integer location = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("user"));
        Integer user = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("mark"));
        Integer mark = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("visited_at"));
        Long visited_at = tmp.map(Long::parseLong).orElse(null);
        return new VisitPostQueryParam(id, location, user, visited_at, mark);
    }

}
