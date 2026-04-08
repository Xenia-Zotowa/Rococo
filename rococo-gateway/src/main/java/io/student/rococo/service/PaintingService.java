package io.student.rococo.service;

import io.student.rococo.data.PaintingEntity;
import io.student.rococo.data.repository.PaintingRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PaintingService {
    private final PaintingRepository paintingRepository;

    public PaintingService(PaintingRepository paintingRepository) {
        this.paintingRepository = paintingRepository;
    }

    @Transactional(readOnly = true)
    public io.student.rococo.dto.PageableResponse<io.student.rococo.dto.PaintingDTO> getList(org.springframework.data.domain.Pageable pageable) {
        try {
            var result = paintingRepository.findAll(pageable);
            return new io.student.rococo.dto.PageableResponse<>(
                    result.getContent().stream()
                            .map(this::toDTO).toList(),
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages(),
                    result.isLast(),
                    result.isFirst());
        } catch (Exception e) {
            return new io.student.rococo.dto.PageableResponse<>(List.of(), 0, 1, 0, 0, true, true);
        }
    }

    @Transactional(readOnly = true)
    public io.student.rococo.dto.PaintingDTO findById(java.util.UUID id) {
        try {
            return paintingRepository.findById(id).map(this::toDTO).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<io.student.rococo.dto.PaintingDTO> findByArtistId(java.util.UUID artistId) {
        try {
            return paintingRepository.findByArtistId(artistId).stream().map(this::toDTO).toList();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Transactional
    public io.student.rococo.dto.PaintingDTO updatePainting(java.util.UUID id, io.student.rococo.dto.PaintingPatchDTO patch) {
        PaintingEntity entity = paintingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Painting not found"));
        if (patch.getTitle() != null) entity.setTitle(patch.getTitle());
        if (patch.getDescription() != null) entity.setDescription(patch.getDescription());
        if (patch.getContent() != null) entity.setContent(patch.getContent());
        var updated = paintingRepository.save(entity);
        return toDTO(updated);
    }


    private io.student.rococo.dto.PaintingDTO toDTO(io.student.rococo.data.PaintingEntity entity) {
        try {
            var museum = toMuseumDTO(entity.getMuseum());
            return io.student.rococo.dto.PaintingDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .description(entity.getDescription())
                    .content(entity.getContent())
                    .museum(museum)
                    .build();
        } catch (Exception e) {
            return io.student.rococo.dto.PaintingDTO.builder().id(entity.getId()).build();
        }
    }

    private io.student.rococo.dto.MuseumDTO toMuseumDTO(io.student.rococo.data.MuseumEntity museum) {
        if (museum == null) return null;
        return io.student.rococo.dto.MuseumDTO.builder()
                .id(museum.getId())
                .title(museum.getTitle())
                .description(museum.getDescription())
                .photo(museum.getPhoto())
                .build();
    }
}