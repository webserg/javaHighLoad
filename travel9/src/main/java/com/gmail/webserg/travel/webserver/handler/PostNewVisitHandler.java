package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.VisitPostQueryParam;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.nio.ByteBuffer;

public class PostNewVisitHandler implements HttpHandler {
    private final ObjectMapper mapper = Config.getInstance().getMapper();
    private final ByteBuffer answer = ByteBuffer.wrap("{}".getBytes());

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                    try {
                        VisitPostQueryParam q = mapper.readValue(data, VisitPostQueryParam.class);
                        if (q.notNewValid() || DataBase.getDb().getVisit(q.id).isPresent()) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                            return;
                        }
                        exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                        exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
                        exch.getResponseSender().send(answer);
                        DataBase.getDb().addVisit(q);

                    } catch (Throwable ex) {
                        exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                    }
                }
        );
    }


}
