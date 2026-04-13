package io.student.rococo.controller;

import io.student.rococo.dto.ArtistDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ArtistRestController {
    private final ArtistService artistService;

    public ArtistRestController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable UUID id) {
        return ResponseEntity.ok(artistService.findById(id));
    }

    @GetMapping("/artist")
    public ResponseEntity<PageableResponse> getArtists(
            Pageable pageable,
            @RequestParam(required = false) String name,
            UriComponentsBuilder uriBuilder) {
        return ResponseEntity.ok(artistService.getList(pageable, name));
    }

    @PostMapping("/artist")
    public ResponseEntity<io.student.rococo.dto.ArtistDTO> createArtist(@Valid @RequestBody io.student.rococo.dto.ArtistDTO dto) {
        var artist = artistService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(artist);
    }

    @PatchMapping("/artist")
    public ResponseEntity<io.student.rococo.dto.ArtistDTO> updateArtist(
            @Valid @RequestBody io.student.rococo.dto.ArtistPatchDTO patch) {
        var updated = artistService.update(patch.getId(), patch);
        return ResponseEntity.ok(updated);
    }
}