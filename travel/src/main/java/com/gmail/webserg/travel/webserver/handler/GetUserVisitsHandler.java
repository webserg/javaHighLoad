package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetUserVisitsHandler implements HttpHandler {
    private final ObjectMapper objectMapper = Config.getInstance().getMapper();

    private ByteBuffer getMessage(String message) {
        ByteBuffer buffer;
        try {
            byte[] m = message.getBytes("UTF-8");
            buffer = ByteBuffer.allocateDirect(m.length);
            buffer.put(m);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {

            UserVisitsRequest req = getRequest(q);
            if (req.id == null) {
                exch.setStatusCode(StatusCodes.NOT_FOUND);
                exch.endExchange();
                return;
            }
            Optional<User> user = DataBase.getDb().getUser(req.id);
            if (!user.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND);
                exch.endExchange();
                return;
            }
            List<UserVisits> resp = DataBase.getDb().getUserVisits(user.get(), req);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final ObjectMapper mapper = new ObjectMapper();
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, "UTF-8");
            Object value = "{}";
            if (resp.size() != 0) {
                value = new UserVisistsResponse(resp);
            }
            mapper.writeValue(out, value);
            exch.getResponseSender().send(ByteBuffer.wrap(out.toByteArray()));
            exch.endExchange();

        } catch (NumberFormatException ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST);
            exch.endExchange();
        }
    }

    @SuppressWarnings("unchecked")
    private UserVisitsRequest getRequest(Map q) {
        Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
        Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("toDate"));
        Long toDate = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("fromDate"));
        Long fromDate = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("country"));
        String country = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("toDistance"));
        Integer toDistance = tmp.map(Integer::parseInt).orElse(null);
        return new UserVisitsRequest(id, toDate, fromDate, country, toDistance);
    }

}
