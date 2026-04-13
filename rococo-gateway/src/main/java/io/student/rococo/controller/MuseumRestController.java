package io.student.rococo.controller;

import io.student.rococo.dto.MuseumDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.MuseumService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MuseumRestController {
    private final MuseumService museumService;

    public MuseumRestController(MuseumService museumService) {
        this.museumService = museumService;
    }

    @GetMapping("/museum")
    public PageableResponse<MuseumDTO> getMuseums(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) java.util.UUID countryId,
            UriComponentsBuilder uriBuilder) {
        return museumService.getList(pageable, title, countryId);
    }

    @GetMapping("/museum/{id}")
    public ResponseEntity<MuseumDTO> getMuseum(@PathVariable UUID id,
                                               UriComponentsBuilder uriBuilder) {
        var museum = museumService.findById(id);
        return ResponseEntity.ok(museum);
    }

    @PatchMapping("/museum")
    public ResponseEntity<MuseumDTO> updateMuseum(
            @Valid @RequestBody io.student.rococo.dto.MuseumPatchDTO patch,
            UriComponentsBuilder uriBuilder) {
        var updated = museumService.update(patch.getId(), patch);
        URI location = uriBuilder.path("/museum/{id}").buildAndExpand(updated.getId()).toUri();
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/museum")
    public ResponseEntity<MuseumDTO> create(
            @Valid @RequestBody MuseumDTO dto,
            UriComponentsBuilder uriBuilder) {
        // This endpoint is part of the spec based on user request for PUT /api/museum
        URI location = uriBuilder.path("/museum/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(dto);
    }
}