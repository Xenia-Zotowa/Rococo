package io.student.rococo.service;

import io.student.rococo.data.CountryEntity;
import io.student.rococo.data.MuseumEntity;
import io.student.rococo.data.repository.CountryRepository;
import io.student.rococo.data.repository.MuseumRepository;
import io.student.rococo.dto.GeoDTO;
import io.student.rococo.dto.MuseumDTO;
import io.student.rococo.dto.PageableResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MuseumService {
    private final MuseumRepository museumRepository;
    private final CountryRepository countryRepository;

    public MuseumService(MuseumRepository museumRepository, CountryRepository countryRepository) {
        this.museumRepository = museumRepository;
        this.countryRepository = countryRepository;
    }

    public PageableResponse<MuseumDTO> getList(Pageable pageable, String title,
                                               java.util.UUID countryId) {
        var museums = countryId != null
                ? museumRepository.findWithCountryId(title, countryId, pageable)
                : (title != null ? museumRepository.findByTitleContainingIgnoreCase(title, pageable)
                : museumRepository.findAll(pageable));

        var dtos = museums.getContent().stream()
                .map(this::toDTO)
                .toList();

        return new PageableResponse<>(dtos,
                museums.getNumber(),
                museums.getSize(),
                museums.getTotalElements(),
                museums.getTotalPages(),
                museums.isLast(),
                museums.isFirst());
    }

    public MuseumDTO findById(java.util.UUID id) {
        var museum = museumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Museum not found: " + id));
        return toDTO(museum);
    }

    @Transactional
    public MuseumDTO update(java.util.UUID id, io.student.rococo.dto.MuseumPatchDTO patch) {
        var museum = museumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Museum not found: " + id));

        if (patch.getTitle() != null) {
            museum.setTitle(patch.getTitle());
        }
        if (patch.getDescription() != null) {
            museum.setDescription(patch.getDescription());
        }
        if (patch.getPhoto() != null) {
            museum.setPhoto(patch.getPhoto());
        }
        if (patch.getGeo() != null) {
            var geo = patch.getGeo();
            if (geo.getCity() != null) {
                museum.setCity(geo.getCity());
            }
            if (geo.getCountry() != null && geo.getCountry().getId() != null) {
                CountryEntity country = countryRepository.findById(geo.getCountry().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Country not found"));
                museum.setCountry(country);
            } else if (geo.getCountry() != null && geo.getCountry().getId() == null) {
                museum.setCountry(null);
            }
        }

        var updated = museumRepository.save(museum);
        return toDTO(updated);
    }

    private MuseumDTO toDTO(MuseumEntity museum) {
        var geo = museum.getCity() != null || museum.getCountry() != null
                ? GeoDTO.builder()
                .city(museum.getCity())
                .country(museum.getCountry() != null
                        ? io.student.rococo.dto.CountryDTO.builder()
                        .id(museum.getCountry().getId())
                        .name(museum.getCountry().getName())
                        .build()
                        : null)
                .build()
                : null;

        return MuseumDTO.builder()
                .id(museum.getId())
                .title(museum.getTitle())
                .description(museum.getDescription())
                .photo(museum.getPhoto())
                .geo(geo)
                .build();
    }
}