package com.mateusz.pwc.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusz.pwc.model.CountryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileLoader {
    private static final TypeReference<List<CountryDto>> COUNTRY_REF = new TypeReference<>() {
    };
    public static final String FILE_PATH = "src/main/resources/countries.json";
    private final ObjectMapper objectMapper;

    public List<CountryDto> loadCountries() {
        try {
            String json = Files.readString(Paths.get(FILE_PATH));
            return objectMapper.readValue(json, COUNTRY_REF);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Couldn't load countries from path: %s", FILE_PATH));
        }
    }
}
