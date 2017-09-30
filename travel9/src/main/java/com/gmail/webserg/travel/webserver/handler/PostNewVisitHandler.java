package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectReader;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.VisitPostQueryParam;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class PostNewVisitHandler implements HttpHandler {
    private static final ObjectReader reader = Config.getInstance().getMapper().readerFor(VisitPostQueryParam.class);

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                    try {
                        VisitPostQueryParam q = reader.readValue(data);
                        if (q.notNewValid() || DataBase.getDb().getVisit(q.id).isPresent()) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                            return;
                        }
                        exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                        exch.getResponseSender().send(Utils.POST_ANSWER.duplicate());
                        DataBase.getDb().addVisit(q);

                    } catch (Throwable ex) {
                        exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                    }
                }
        );
    }


}
