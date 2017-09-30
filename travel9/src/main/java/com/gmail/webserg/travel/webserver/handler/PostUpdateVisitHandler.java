package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectReader;
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
    private static final ObjectReader reader = Config.getInstance().getMapper().readerFor(new TypeReference<Map
            <String, String>>() {
    });

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) exch.getQueryParameters().get("id"));
            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
            if (id == null) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }
            Optional<Visit> visit = DataBase.getDb().getVisit(id);
            if (!visit.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                        try {
                            VisitPostQueryParam q = validation(exch, data);
                            if (q == null) return;

                            if (q.notUpdateValid() || !visit.isPresent()) {
                                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                                return;
                            }
                            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                            exch.getResponseSender().send(Utils.POST_ANSWER.duplicate());
                            DataBase.getDb().updateVisit(visit.get(), q);

                        } catch (Throwable ex) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                        }
                    }
            );
        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

    private VisitPostQueryParam validation(HttpServerExchange exch, byte[] data) throws java.io.IOException {
        Map<String, String> map = reader.readValue(data);
        if (map.values().contains(null)) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
            return null;
        }
        VisitPostQueryParam newVisit = new VisitPostQueryParam();
        if (map.get("location") != null) {
            newVisit.setLocation(Integer.parseUnsignedInt(map.get("location")));
        }
        if (map.get("user") != null) {
            newVisit.setUser(Integer.parseUnsignedInt(map.get("user")));
        }
        if (map.get("visited_at") != null) {
            newVisit.setVisited_at(Long.parseLong(map.get("visited_at")));
        }
        if (map.get("mark") != null) {
            newVisit.setMark(Integer.parseUnsignedInt(map.get("mark")));
        }
        return newVisit;
    }


}
