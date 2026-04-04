package io.student.rococo.service;

import io.student.rococo.data.PaintingEntity;
import io.student.rococo.data.repository.PaintingRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpMessageNotReadableException;
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
    public io.student.rococo.data.PaintingEntity updatePainting(io.student.rococo.dto.PaintingDTO painting) {
        PaintingEntity entity = paintingRepository.findById(painting.getId())
                .orElseThrow(() -> new IllegalArgumentException("Painting not found"));
        if (painting.getTitle() != null) entity.setTitle(painting.getTitle());
        if (painting.getDescription() != null) entity.setDescription(painting.getDescription());
        if (painting.getContent() != null) entity.setContent(painting.getContent());
        return paintingRepository.save(entity);
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