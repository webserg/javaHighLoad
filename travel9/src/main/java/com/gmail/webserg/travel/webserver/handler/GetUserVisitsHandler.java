package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.UserVisits;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.UserVisistsResponse;
import com.gmail.webserg.travel.webserver.params.UserVisitsRequest;
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
    private static final ObjectWriter writer = Config.getInstance().getMapper().writer();

    @Override
    public void handleRequest(final HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {

            final UserVisitsRequest req = getRequest(q);
            if (req.id == null) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            final Optional<User> user = DataBase.getDb().getUser(req.id);
            if (!user.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            List<UserVisits> resp = DataBase.getDb().getUserVisits(user.get(), req);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            writer.writeValue(out, new UserVisistsResponse(resp));

            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, Utils.CHARSET);
            exch.getResponseSender().send(ByteBuffer.wrap(out.toByteArray()));

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
