package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class PostUpdateLocationHandler implements HttpHandler {
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
            Optional<Location> location = DataBase.getDb().getLocation(id);
            if (!location.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                        try {
                            if (validation(exch, data)) return;
                            Location newLocation = mapper.readValue(data, Location.class);
                            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                            exch.getResponseSender().send(Utils.POST_ANSWER);
                            DataBase.getDb().updateLocation(location.get(), newLocation);

                        } catch (Throwable ex) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                        }
                    }
            );

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
