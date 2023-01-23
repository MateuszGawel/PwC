package com.mateusz.pwc.controller;

import com.mateusz.pwc.model.RouteDto;
import com.mateusz.pwc.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/routing")
@Slf4j
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping("{origin}/{destination}")
    RouteDto getRoute(@PathVariable String origin, @PathVariable String destination) {
        log.atInfo().log("Calculating route. Origin: {}, Destination: {}", origin, destination);
        return routeService.findRoute(origin, destination);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleResponseStatusException(ResponseStatusException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }
}
