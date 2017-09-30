package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public abstract class GetEntityIdHandler<T> implements HttpHandler {

    private static final ObjectMapper mapper = Config.getInstance().
            getMapper().configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);


    @Override
    public void handleRequest(final HttpServerExchange exch) throws Exception {
        final Map q = exch.getQueryParameters();
        try {
            Optional<String> tmp = Utils.toString((ArrayDeque<String>) q.get("id"));
            final Integer id = tmp.map(Integer::parseUnsignedInt).orElse(null);
            Optional<T> entity = getEntity(id);
            if (!entity.isPresent()) {
                exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
                return;
            }
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            mapper.writeValue(out, entity.get());
            exch.getResponseHeaders().put(Headers.CONTENT_TYPE, Utils.CONTENT_TYPE);
            exch.getResponseHeaders().put(Headers.CONTENT_ENCODING, Utils.CHARSET);
            exch.getResponseSender().send(ByteBuffer.wrap(out.toByteArray()));
        } catch (final NumberFormatException ex) {
            exch.setStatusCode(StatusCodes.NOT_FOUND).endExchange();
        } catch (final Throwable ex) {
            exch.setStatusCode(StatusCodes.BAD_REQUEST).endExchange();
        }
    }

    abstract Optional<T> getEntity(int id);
}
