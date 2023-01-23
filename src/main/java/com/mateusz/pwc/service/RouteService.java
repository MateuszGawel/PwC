package com.mateusz.pwc.service;

import com.mateusz.pwc.model.CountryDto;
import com.mateusz.pwc.model.RouteDto;
import com.mateusz.pwc.utils.FileLoader;
import com.mateusz.pwc.utils.GraphCreator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteService {

    private Map<String, CountryDto> countries;
    private final FileLoader fileLoader;
    private DijkstraShortestPath<CountryDto, DefaultEdge> dijkstraShortestPath;

    public RouteDto findRoute(String origin, String destination) {
        CountryDto originCountryDto = Optional.ofNullable(countries.get(origin.toUpperCase()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The origin country '%s' does not exist.", origin)));
        CountryDto destinationCountryDto = Optional.ofNullable(countries.get(destination.toUpperCase()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The destination country '%s' does not exist.", destination)));

        List<String> routeCodes = getCountriesRoute(originCountryDto, destinationCountryDto);

        return new RouteDto(routeCodes);
    }

    private List<String> getCountriesRoute(CountryDto originCountryDto, CountryDto destinationCountryDto) {
        GraphPath<CountryDto, DefaultEdge> graphPath = Optional.ofNullable(dijkstraShortestPath.getPath(originCountryDto, destinationCountryDto))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        String.format("The route between '%s' and '%s' not found.", originCountryDto.getCca3(), destinationCountryDto.getCca3())));

        return graphPath.getVertexList().stream()
                .map(CountryDto::getCca3)
                .collect(Collectors.toList());
    }

    @PostConstruct
    private void initialize() {
        log.atInfo().log("Loading countries to memory...");
        countries = fileLoader.loadCountries().stream().collect(Collectors.toMap(CountryDto::getCca3, c -> c));
        log.atInfo().log("Loaded {} countries.", countries.size());

        Graph<CountryDto, DefaultEdge> graph = GraphCreator.createGraph(countries);
        dijkstraShortestPath = new DijkstraShortestPath<>(graph);
    }
}
