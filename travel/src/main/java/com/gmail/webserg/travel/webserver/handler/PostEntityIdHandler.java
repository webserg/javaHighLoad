package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostEntityIdHandler implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
            if (id == null) {
                exch.getResponseHeaders().put(Headers.STATUS, StatusCodes.NOT_FOUND);
                exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
                exch.getResponseSender().send(StatusCodes.getReason(StatusCodes.NOT_FOUND));
                return;
            }
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send("{}");
        } catch (Throwable ex) {
            exch.getResponseHeaders().put(Headers.STATUS, StatusCodes.BAD_REQUEST);
        }
    }

}
