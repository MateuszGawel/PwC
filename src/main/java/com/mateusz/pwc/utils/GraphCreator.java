package com.mateusz.pwc.utils;

import com.mateusz.pwc.model.CountryDto;
import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphCreator {
    public static Graph<CountryDto, DefaultEdge> createGraph(Map<String, CountryDto> countries) {
        Graph<CountryDto, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        try {
            addVertexes(graph, countries);
            addEdges(graph, countries);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not initialize countries graph.", e);
        }
        return graph;
    }

    private static void addVertexes(Graph<CountryDto, DefaultEdge> graph, Map<String, CountryDto> countries) {
        countries.values().forEach(graph::addVertex);
    }

    private static void addEdges(Graph<CountryDto, DefaultEdge> graph, Map<String, CountryDto> countries) {
        for (CountryDto countryDto : countries.values()) {
            List<Pair<CountryDto, CountryDto>> neighboringPairs = getNeighboringPairs(countryDto, countries);
            neighboringPairs.forEach(p -> graph.addEdge(p.getFirst(), p.getSecond()));
        }
    }

    private static List<Pair<CountryDto, CountryDto>> getNeighboringPairs(CountryDto country, Map<String, CountryDto> countries) {
        return country.getBorders().stream()
                .map(countries::get)
                .map(n -> new Pair<>(country, n))
                .collect(Collectors.toList());
    }
}
