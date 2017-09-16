package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public abstract class GetEntityIdHandler<T> implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();


    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
            Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
            Optional<T> user = getEntity(id);
            if (!user.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            String jsonInString = objectMapper.writeValueAsString(user.get());
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            exch.getResponseSender().send(Utils.getMessage(jsonInString));

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
        }
    }

    abstract Optional<T> getEntity(int id);
}
