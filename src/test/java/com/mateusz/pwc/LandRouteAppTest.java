package com.mateusz.pwc;

import com.mateusz.pwc.model.RouteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LandRouteAppTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testRouteProvided() {
        RouteDto result = restTemplate.getForObject(getRequestUrl("PRT", "POL"), RouteDto.class);

        assertThat(result).isNotNull();
        assertThat(result.getRoute()).isEqualTo(List.of("PRT", "ESP", "FRA", "DEU", "POL"));
    }

    @Test
    void testBadOrigin() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getRequestUrl("XXX", "POL"), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testBadDestination() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getRequestUrl("PRT", "XXX"), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private String getRequestUrl(String origin, String destination) {
        return String.format("http://localhost:%d/routing/%s/%s", port, origin, destination);
    }
}
