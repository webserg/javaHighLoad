package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.webserver.DataBase;
import com.gmail.webserg.travel.webserver.params.LocationAvgRequest;
import com.gmail.webserg.travel.webserver.params.LocationAvgResponse;
import com.networknt.config.Config;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Optional;

public class GetLocationAvgHandler implements HttpHandler {
    private static final ObjectWriter writer = Config.getInstance().getMapper().writerFor(LocationAvgResponse.class);

    @Override
    public void handleRequest(HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {

            LocationAvgRequest req = getRequest(q);
            if (req.id == null) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            if (req.gender != null && req.gender.length() > 1) {
                exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
                return;
            }
            Optional<Location> location = DataBase.getDb().getLocation(req.id);
            if (!location.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            double resp = DataBase.getDb().getLocAvgResult(location.get(), req);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
            writer.writeValue(out, new LocationAvgResponse(resp));
            exch.getResponseSender().send(ByteBuffer.wrap(out.toByteArray()));
            out.close();
        } catch (NumberFormatException ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST);
            exch.endExchange();
        }
    }

    @SuppressWarnings("unchecked")
    private LocationAvgRequest getRequest(Map q) {
        Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
        Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("toDate"));
        Long toDate = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("fromDate"));
        Long fromDate = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("gender"));
        String gender = tmp.orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("toAge"));
        Long toAge = tmp.map(Long::parseLong).orElse(null);
        tmp = Utils.toString((ArrayDeque<String>) q.get("fromAge"));
        Long fromAge = tmp.map(Long::parseLong).orElse(null);
        return new LocationAvgRequest(id, toDate, fromDate, toAge, fromAge, gender);
    }

}
