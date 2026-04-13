package io.student.rococo.service;

import io.student.rococo.data.ArtistEntity;
import io.student.rococo.data.repository.ArtistRepository;
import io.student.rococo.dto.ArtistDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public PageableResponse<ArtistDTO> getList(Pageable pageable, String name) {
        var artists = name != null
                ? artistRepository.findByNameContainingIgnoreCase(name, pageable)
                : artistRepository.findAll(pageable);

        var dtos = artists.getContent().stream()
                .map(this::toDTO)
                .toList();

        return new PageableResponse<>(dtos,
                artists.getNumber(),
                artists.getSize(),
                artists.getTotalElements(),
                artists.getTotalPages(),
                artists.isLast(),
                artists.isFirst());
    }

    public ArtistDTO findById(java.util.UUID id) {
        var artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found", id));
        return toDTO(artist);
    }

    @Transactional
    public ArtistDTO update(java.util.UUID id, io.student.rococo.dto.ArtistPatchDTO patch) {
        var artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found", id));
        if (patch.getName() != null) {
            artist.setName(patch.getName());
        }
        if (patch.getBiography() != null) {
            artist.setBiography(patch.getBiography());
        }
        if (patch.getPhoto() != null) {
            artist.setPhoto(patch.getPhoto().getBytes(StandardCharsets.UTF_8));
        }
        var updated = artistRepository.save(artist);
        return toDTO(updated);
    }

    @Transactional
    public ArtistDTO save(ArtistDTO dto) {
        var artist = ArtistEntity.builder()
                .name(dto.getName())
                .biography(dto.getBiography())
                .photo(dto.getPhoto() != null ? dto.getPhoto().getBytes(StandardCharsets.UTF_8) : null)
                .build();
        var saved = artistRepository.save(artist);
        return toDTO(saved);
    }

    private ArtistDTO toDTO(ArtistEntity artist) {
        return ArtistDTO.builder()
                .id(artist.getId())
                .name(artist.getName())
                .biography(artist.getBiography())
                .photo(artist.getPhoto() != null ? new String(artist.getPhoto(), StandardCharsets.UTF_8) : null)
                .build();
    }
}