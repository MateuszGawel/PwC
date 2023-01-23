package com.mateusz.pwc.service;

import com.mateusz.pwc.model.CountryDto;
import com.mateusz.pwc.model.RouteDto;
import com.mateusz.pwc.utils.FileLoader;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RouteServiceTest {

    @Autowired
    RouteService routeService;

    private static final String PRT = "PRT";
    private static final String ESP = "ESP";
    private static final String FRA = "FRA";
    private static final String DEU = "DEU";
    private static final String POL = "POL";

    private static final String USA = "USA";
    private static final CountryDto portugal = new CountryDto(PRT, List.of(ESP));
    private static final CountryDto spain = new CountryDto(ESP, List.of(FRA, PRT));
    private static final CountryDto france = new CountryDto(FRA, List.of(DEU, ESP));
    private static final CountryDto germany = new CountryDto(DEU, List.of(POL, FRA));
    private static final CountryDto poland = new CountryDto(POL, List.of(DEU));
    private static final CountryDto usa = new CountryDto(USA, List.of());
    private static final List<CountryDto> countries = List.of(portugal, spain, france, germany, poland, usa);

    @Test
    public void testSuccess_portugal_to_poland() {
        String origin = "PRT";
        String destination = "POL";

        RouteDto route = routeService.findRoute(origin, destination);

        assertThat(route).isNotNull();
        assertThat(route.getRoute()).isEqualTo(List.of("PRT", "ESP", "FRA", "DEU", "POL"));
    }

    @Test
    public void testSuccess_poland_to_portugal() {
        String origin = "POL";
        String destination = "PRT";

        RouteDto route = routeService.findRoute(origin, destination);

        assertThat(route).isNotNull();
        assertThat(route.getRoute()).isEqualTo(List.of("POL", "DEU", "FRA", "ESP", "PRT"));
    }

    @Test
    public void testWrongOrigin_exceptionThrown() {
        String origin = "XXX";
        String destination = "PRT";

        assertThrows(ResponseStatusException.class, () -> routeService.findRoute(origin, destination));
    }

    @Test
    public void testWrongDestination_exceptionThrown() {
        String origin = "POL";
        String destination = "XXX";

        assertThrows(ResponseStatusException.class, () -> routeService.findRoute(origin, destination));
    }

    @Test
    public void testNoRoute_exceptionThrown() {
        String origin = "POL";
        String destination = "USA";

        assertThrows(ResponseStatusException.class, () -> routeService.findRoute(origin, destination));
    }

    @TestConfiguration
    public static class Configuration {
        @MockBean
        private FileLoader fileLoaderMock;

        @PostConstruct
        public void initMock() {
            when(fileLoaderMock.loadCountries()).thenReturn(countries);
        }
    }
}
