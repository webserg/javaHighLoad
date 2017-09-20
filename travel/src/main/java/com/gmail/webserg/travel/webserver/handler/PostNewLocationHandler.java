package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class PostNewLocationHandler implements HttpHandler {
    private final ObjectMapper mapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        try {
            exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                        try {
                            Location location = mapper.readValue(data, Location.class);
                            if (location.notValid() || DataBase.getDb().getLocation(location.getId()).isPresent()) {
                                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                                return;
                            }
                            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
                            exch.getResponseSender().send("{}");
                            DataBase.getDb().addLocation(location);

                        } catch (Throwable ex) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                        }
                    }
            );

        } catch (Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

        }
    }

}
