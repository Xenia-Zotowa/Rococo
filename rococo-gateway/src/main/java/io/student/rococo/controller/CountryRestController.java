package io.student.rococo.controller;

import io.student.rococo.dto.CountryDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.CountryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class CountryRestController {
    private final CountryService countryService;

    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/country")
    public PageableResponse<CountryDTO> getCountries(
            Pageable pageable,
            @RequestParam(required = false) String name,
            UriComponentsBuilder uriBuilder) {
        return countryService.getList(pageable, name);
    }
}