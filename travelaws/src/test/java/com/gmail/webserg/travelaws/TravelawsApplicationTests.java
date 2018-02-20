package com.gmail.webserg.travelaws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TravelawsApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exampleTest() {
        String body = this.restTemplate.getForObject("/users/1", String.class);
        assertThat(body).isEqualTo("{\"id\":1,\"first_name\":\"serg\",\"last_name\":\"doroshenko\",\"birth_date\":100001,\"gender\":\"m\",\"email\":\"webserg@gmail.com\",\"userVisitsPosition\":0,\"userVisitsSize\":0}");
    }

}
