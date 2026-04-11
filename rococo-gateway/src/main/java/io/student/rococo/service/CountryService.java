package io.student.rococo.service;

import io.student.rococo.data.CountryEntity;
import io.student.rococo.data.repository.CountryRepository;
import io.student.rococo.dto.CountryDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public PageableResponse<CountryDTO> getList(Pageable pageable, String title) {
        var countries = countryRepository.findAllOrdered(pageable);
        var dtos = countries.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageableResponse<>(
                dtos,
                0,
                dtos.size(),
                dtos.size(),
                1,
                true,
                true
        );
    }

    private CountryDTO toDTO(CountryEntity country) {
        return CountryDTO.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}