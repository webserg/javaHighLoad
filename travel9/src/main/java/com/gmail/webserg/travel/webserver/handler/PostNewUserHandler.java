package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectReader;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class PostNewUserHandler implements HttpHandler {
    private static final ObjectReader reader = Config.getInstance().getMapper().readerFor(User.class);

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                    try {
                        User user = reader.readValue(data);
                        if (user.notValid() || DataBase.getDb().getUser(user.getId()).isPresent()) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                            return;
                        }
                        exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                        exch.getResponseSender().send(Utils.POST_ANSWER.duplicate());
                        DataBase.getDb().addUser(user);

                    } catch (Throwable ex) {
                        exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                    }
                }
        );
    }


}
