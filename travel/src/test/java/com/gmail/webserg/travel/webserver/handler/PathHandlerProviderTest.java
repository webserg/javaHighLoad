package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.User;
import com.google.common.collect.Lists;
import com.networknt.client.Http2Client;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Methods;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class PathHandlerProviderTest {
    static final Logger logger = LoggerFactory.getLogger(PathHandlerProviderTest.class);

    @Test
    public void getHandler() throws Exception {
        for (int i = 1; i < 1010; i++)
            testGetUserById(ThreadLocalRandom.current().nextInt(1, 100123));
    }

    @Test
    public void testUsersVisits() throws Exception {
        for (int i = 1; i < 100; i++)
            testUsersVisitById(ThreadLocalRandom.current().nextInt(1, 100123));
    }

    private void testGetUserById(int id) throws ClientException, java.io.IOException {
        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            logger.error("/users/" + id);
            ClientRequest request = new ClientRequest().setPath("/users/" + id).setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();

            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals(200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(body, User.class);
            Assert.assertEquals(id, user.getId());

        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

    public void testUsersVisitById(int id) throws ClientException, java.io.IOException {
        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            ClientRequest request = new ClientRequest().setPath("/users/" + id + "/visits").setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();

            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals( request.toString(),200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            List<UserVisitsResponse> userVisitsResponses = mapper.readValue(body, mapper.getTypeFactory().constructCollectionLikeType(List.class, UserVisitsResponse.class));
            Assert.assertEquals(body, mapper.writeValueAsString(userVisitsResponses));

        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

}