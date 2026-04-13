package io.student.rococo.service;

import io.student.rococo.data.PaintingEntity;
import io.student.rococo.data.repository.PaintingRepository;
import io.student.rococo.data.repository.MuseumRepository;
import io.student.rococo.dto.PaintingDTO;
import io.student.rococo.dto.PaintingPatchDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.dto.MuseumDTO;
import io.student.rococo.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private final MuseumRepository museumRepository;

    public PaintingService(PaintingRepository paintingRepository, MuseumRepository museumRepository) {
        this.paintingRepository = paintingRepository;
        this.museumRepository = museumRepository;
    }

    public PageableResponse<PaintingDTO> getList(Pageable pageable, String title, Long museumId) {
        Page<PaintingEntity> result;

        if (title != null && museumId != null) {
            result = paintingRepository.findByTitleContainingAndMuseumId(title, UUID.fromString(museumId.toString()), pageable);
        } else if (title != null) {
            result = paintingRepository.findByTitleContaining(title, pageable);
        } else {
            result = paintingRepository.findAll(pageable);
        }

        var filteredContent = result.getContent().stream()
                .map(this::toDTO)
                .toList();

        return new PageableResponse<>(
                filteredContent,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast(),
                result.isFirst()
        );
    }

    public PaintingDTO findById(UUID id) {
        return paintingRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Painting not found", id));
    }

    public List<PaintingDTO> findByArtistId(UUID artistId) {
        return paintingRepository.findByArtistId(artistId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public PaintingDTO updatePainting(UUID id, PaintingPatchDTO patch) {
        PaintingEntity entity = paintingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Painting not found", id));

        if (patch.getTitle() != null) entity.setTitle(patch.getTitle());
        if (patch.getDescription() != null) entity.setDescription(patch.getDescription());
        if (patch.getContent() != null) entity.setContent(patch.getContent().getBytes(StandardCharsets.UTF_8));

        var updated = paintingRepository.save(entity);
        return toDTO(updated);
    }

    @Transactional
    public PaintingDTO save(PaintingDTO dto) {
        PaintingEntity entity = PaintingEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent() != null ? dto.getContent().getBytes(StandardCharsets.UTF_8) : null)
                .museum(dto.getMuseum() != null ? toMuseumEntity(dto.getMuseum()) : null)
                .build();
        var saved = paintingRepository.save(entity);
        return toDTO(saved);
    }

    private PaintingDTO toDTO(PaintingEntity entity) {
        var museumDTO = toMuseumDTO(entity.getMuseum());
        return PaintingDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .content(entity.getContent() != null ? new String(entity.getContent(), StandardCharsets.UTF_8) : null)
                .museum(museumDTO)
                .build();
    }

    private MuseumDTO toMuseumDTO(io.student.rococo.data.MuseumEntity museum) {
        if (museum == null) return null;
        return MuseumDTO.builder()
                .id(museum.getId())
                .title(museum.getTitle())
                .description(museum.getDescription())
                .photo(museum.getPhoto() != null ? new String(museum.getPhoto(), StandardCharsets.UTF_8) : null)
                .build();
    }

    private io.student.rococo.data.MuseumEntity toMuseumEntity(MuseumDTO dto) {
        if (dto == null || dto.getId() == null) return null;
        return museumRepository.findById(dto.getId()).orElse(null);
    }
}