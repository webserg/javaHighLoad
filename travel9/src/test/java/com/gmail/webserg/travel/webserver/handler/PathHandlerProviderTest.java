package com.gmail.webserg.travel.webserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.webserg.travel.domain.Location;
import com.gmail.webserg.travel.domain.User;
import com.gmail.webserg.travel.domain.UserVisits;
import com.gmail.webserg.travel.domain.Visit;
import com.gmail.webserg.travel.webserver.params.LocationAvgResponse;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;


public class PathHandlerProviderTest {
    static final Logger logger = LoggerFactory.getLogger(PathHandlerProviderTest.class);

    @Test
    public void getUsers() throws Exception {
        for (int i = 1; i < 1010; i++)
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

    @Test
    public void testLocationAvg() throws Exception {
        testGetLocationAvg(2765, 2.66667);
        testGetLocationAvg(3586, 2.81818);
//        testGetLocationAvg(2765,2.66667);
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
            Thread.sleep(10);
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals(200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(body, User.class);
            Assert.assertEquals(id, user.getId());
            System.out.println(body);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            throw new ClientException(e);
        } finally {
            IoUtils.safeClose(connection);
        }

    }

    private void testGetLocationsById(Integer id) throws ClientException, java.io.IOException {
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
            Thread.sleep(10);
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            System.out.println("/locations/" + id);
            Assert.assertEquals("/locations/" + id, 200, statusCode);
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

    private void testGetLocationAvg(Integer id, Double avg) throws ClientException, java.io.IOException {
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
            ClientRequest request = new ClientRequest().setPath("/locations/" + id + "/avg").setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();
            Thread.sleep(100);
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            System.out.println("/locations/" + id);
            Assert.assertEquals("/locations/" + id, 200, statusCode);
            Assert.assertNotNull(body);
            ObjectMapper mapper = new ObjectMapper();
            LocationAvgResponse location = mapper.readValue(body, LocationAvgResponse.class);
            Assert.assertEquals(avg, location.getAvg());

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
            connection = client.connect(new URI("http://localhost"), Http2Client.WORKER, Http2Client.SSL,
                    Http2Client.POOL, false ? OptionMap.create(UndertowOptions.ENABLE_HTTP2, true) : OptionMap.EMPTY).get();
        } catch (Exception e) {
            throw new ClientException(e);
        }
        try {
            logger.info("/visits/" + id);
            ClientRequest request = new ClientRequest().setPath("/visits/" + id).setMethod(Methods.GET);
            final AtomicReference<ClientResponse> reference = new AtomicReference<>();

            connection.sendRequest(request, client.createClientCallback(reference, latch));

            latch.await();
            Thread.sleep(10);
            int statusCode = reference.get().getResponseCode();
            String body = reference.get().getAttachment(Http2Client.RESPONSE_BODY);
            Assert.assertEquals("/visits/" + id, 200, statusCode);
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
            Thread.sleep(10);
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
    public void testPostNewUser() throws IOException {
        for (int i = 1; i <= 100; i++) {

            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(
                        "http://localhost/users/new");

                StringEntity input = new StringEntity("{\n" +
                        "        \"id\": " + (245000 + i) + ",\n" +
                        "        \"email\": \"foobar@mail.ru\",\n" +
                        "        \"first_name\": \"Маша\",\n" +
                        "        \"last_name\": \"Пушкина\",\n" +
                        "        \"gender\": \"f\",\n" +
                        "        \"birth_date\": 365299200\n" +
                        "    }", Charset.forName("UTF8"));
                input.setContentType("application/json");
                input.setContentEncoding("UTF-8");
                postRequest.setEntity(input);

                HttpResponse response = httpClient.execute(postRequest);

                if (response.getStatusLine().getStatusCode() != 200) {
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
                Thread.sleep(20);
                testGetVisitsById(245000 + i);


            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    public void testPostNewUserNull() throws IOException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/users/new");

            StringEntity input = new StringEntity("{\n" +
                    "  \"first_name\": null,\n" +
                    "  \"last_name\": \"Даныкалан\",\n" +
                    "  \"gender\": \"m\",\n" +
                    "  \"id\": \"broken\",\n" +
                    "  \"birth_date\": -739497600,\n" +
                    "  \"email\": \"noghanactayt@inbox.ru\"\n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 400) {
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

            testGetVisitsById(245000);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testPostUpdateUser() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/users/1");

            StringEntity input = new StringEntity("{\n" +
                    "        \"email\": \"foobar123@mail.ru\",\n" +
                    "        \"first_name\": \"Маша\",\n" +
                    "        \"birth_date\": 365299200\n" +
                    "    }", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            String body = new String(response.getEntity().getContent().readAllBytes());
            System.out.println(body);
            Assert.assertEquals("{}", body);
            Assert.assertNotNull(body);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));


            httpClient.getConnectionManager().shutdown();

            testGetVisitsById(1);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testPostUpdateUser2() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/users/2");

            StringEntity input = new StringEntity("{\n" +
                    "        \"email\": \"foobar123@mail.ru\",\n" +
                    "        \"first_name\": \"Маша\",\n" +
                    "        \"last_name\": \"Пушкина\",\n" +
                    "        \"gender\": \"f\"\n" +
                    "    }", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
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

            testGetUserById(2);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testPostNewVisit() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/new");

            StringEntity input = new StringEntity("{\n" +
                    "  \"id\": 10000977,\n" +
                    "  \"user\": 7164,\n" +
                    "  \"visited_at\": 1199182476,\n" +
                    "  \"location\": 796,\n" +
                    "  \"mark\": 3333\n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
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

            testGetLocationAvg(796, 60.92982);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testPostUpdateVisit() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/10000977");

            StringEntity input = new StringEntity("{\n" +
                    "  \"user\": 7165,\n" +
                    "  \"visited_at\": 1199182476,\n" +
                    "  \"location\": 797,\n" +
                    "  \"mark\": 289\n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
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

            testGetLocationAvg(796, 7.52632);
            testGetLocationAvg(797, 7.35);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPostUpdateVisit2() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/10000977");

            StringEntity input = new StringEntity("{\n" +
                    "  \"visited_at\": 1199182476,\n" +
                    "  \"mark\": 10000 \n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
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

            testGetVisitsById(10000977);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testPostUpdateVisitNotFound() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/28752289");

            StringEntity input = new StringEntity("{\n" +
                    " \"mark\": 2,\n" +
                    "  \"user\": 8707,\n" +
                    "  \"location\": 3393" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 404) {
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

    @Test
    public void testPostUpdateVisitNotFound30115000() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/30115000");

            StringEntity input = new StringEntity("{\n" +
                    "  \"user\": 8161\n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 404) {
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


    @Test
    public void testPostUpdateLocation() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/locations/1402");

            StringEntity input = new StringEntity("{\n" +
                    " \"city\": null" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 400) {
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

    @Test
    public void testPostUpdateVisitPlace() throws IOException, InterruptedException {

        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://localhost/visits/156");

            StringEntity input = new StringEntity("{\n" +
                    "  \"user\": 3,\n" +
                    "  \"visited_at\": 1199182476,\n" +
                    "  \"mark\": 10000 \n" +
                    "}", Charset.forName("UTF8"));
            input.setContentType("application/json");
            input.setContentEncoding("UTF-8");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);
            Thread.sleep(20);
            if (response.getStatusLine().getStatusCode() != 200) {
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

            testGetVisitsById(10000977);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (ClientException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void smallTest() {
        System.out.println("c:\\asdasd\\adasd\\users_10.json".replaceAll("[^0-9]", ""));
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit(1, 1, 1, 1, 1));
        visits.add(new Visit(2, 1, 1, 2, 1));
        visits.add(new Visit(3, 1, 1, 3, 1));
        visits.add(new Visit(4, 1, 1, 4, 1));
        System.out.println(visits);
        visits.remove(new Visit(2, 1, 1, 2, 1));
        System.out.println(visits);
        Map<Integer, Visit> map = visits.stream().collect(Collectors.toMap(Visit::getId, Function.identity()));
        System.out.println(map);
        map.remove(3);
        System.out.println(map);
        Visit v1 = new Visit(1, 1, 1, 1, 1);
        Visit v2 = new Visit(1, 1, 1, 1, 1);
        Assert.assertEquals(v1, v2);
    }


}

