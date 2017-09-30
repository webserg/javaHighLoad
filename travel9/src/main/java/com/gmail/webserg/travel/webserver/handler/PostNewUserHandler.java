package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class PostNewUserHandler implements HttpHandler {
    private final ObjectMapper mapper = Config.getInstance().getMapper();

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        exch.getRequestReceiver().receiveFullBytes((exchange, data) -> {
                    try {
                        User user = mapper.readValue(data,User.class);
                        if (user.notValid() || DataBase.getDb().getUser(user.getId()).isPresent()) {
                            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                            return;
                        }
                        exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
                        exch.getResponseSender().send(Utils.POST_ANSWER);
                        DataBase.getDb().addUser(user);

                    } catch (Throwable ex) {
                        exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();

                    }
                }
        );
    }


}
