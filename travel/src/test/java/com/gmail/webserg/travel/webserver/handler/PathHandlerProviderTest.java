package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.UserVisits;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.params.UserVisistsResponse;
import com.networknt.client.Http2Client;
import com.networknt.exception.ClientException;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Methods;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class PathHandlerProviderTest {
    static final Logger logger = LoggerFactory.getLogger(PathHandlerProviderTest.class);

    @Test
    public void getUsers() throws Exception {
        for (int i = 1; i < 5010; i++)
            testGetUserById(ThreadLocalRandom.current().nextInt(1, 100125));
    }

    @Test
    public void getLocations() throws Exception {
        for (int i = 1; i < 1010; i++)
            testGetLocationsById(ThreadLocalRandom.current().nextInt(1, 76846));
    }

    @Test
    public void getVisists() throws Exception {
        for (int i = 1; i < 1010; i++)
            testGetVisitsById(ThreadLocalRandom.current().nextInt(1, 1001241));
    }


    @Test
    public void testUsersVisits() throws Exception {
        for (int i = 1; i < 1000; i++)
            testUsersVisitById(ThreadLocalRandom.current().nextInt(1, 100125));
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
            logger.info("/users/" + id);
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

    private void testGetLocationsById(int id) throws ClientException, java.io.IOException {
        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            logger.info("/locations/" + id);
            ClientRequest request = new ClientRequest().setPath("/locations/" + id).setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();

            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals("/locations/" + id,200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            Location location = mapper.readValue(body, Location.class);
            Assert.assertEquals(id, location.getId());

        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

    private void testGetVisitsById(int id) throws ClientException, java.io.IOException {
        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            logger.info("/visits/" + id);
            ClientRequest request = new ClientRequest().setPath("/visits/" + id).setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();

            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals("/visits/" + id,200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            Visit visit = mapper.readValue(body, Visit.class);
            Assert.assertEquals(id, visit.getId());

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
            Assert.assertEquals(request.toString(), 200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            UserVisistsResponse userVisitsRespons = mapper.readValue(body, UserVisistsResponse.class);
//            Assert.assertEquals(body, mapper.writeValueAsString(userVisitsRespons.getVisits()));
            System.out.println(mapper.writeValueAsString(userVisitsRespons.getVisits()));

        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

    @Test
    public void testPostUsersVisit() throws ClientException, java.io.IOException {
        final Http2Client client = Http2Client.getInstance();
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection;
        try {
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL, Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            ClientRequest request = new ClientRequest().setPath("/users/1").setMethod(Methods.POST);

            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch, "{\n" +
                    "        \"id\": 245000,\n" +
                    "        \"email\": \"foobar@mail.ru\",\n" +
                    "        \"first_name\": \"Маша\",\n" +
                    "        \"last_name\": \"Пушкина\",\n" +
                    "        \"gender\": \"f\",\n" +
                    "        \"birth_date\": 365299200\n" +
                    "    }"));

            latch.await();

            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals(request.toString(), 200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            List<UserVisits> userVisitsRespons = mapper.readValue(body, mapper.getTypeFactory().constructCollectionLikeType(List.class, UserVisits.class));
            Assert.assertEquals(body, mapper.writeValueAsString(userVisitsRespons));

        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

    @Test
    public void testPost() throws IOException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/users/new");

            StringEntity input = new StringEntity("{\n" +
                    "        \"id\": 245000,\n" +
                    "        \"email\": \"foobar@mail.ru\",\n" +
                    "        \"first_name\": \"Маша\",\n" +
                    "        \"last_name\": \"Пушкина\",\n" +
                    "        \"gender\": \"f\",\n" +
                    "        \"birth_date\": 365299200\n" +
                    "    }");
            input.setContentType("application/json");
            input.setContentEncoding("UTF8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            httpClient.getConnectionManager().shutdown();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}

