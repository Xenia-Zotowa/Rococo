package io.student.rococo.controller;

import io.student.rococo.dto.ArtistDTO;
import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.ArtistService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/artist")
    public PageableResponse<ArtistDTO> getArtists(
            Pageable pageable,
            @RequestParam(required = false) String name,
            UriComponentsBuilder uriBuilder) {
        return artistService.getList(pageable, name);
    }

    @GetMapping("/artist/{id}")
    public ResponseEntity<ArtistDTO> getArtist(@PathVariable UUID id) {
        var artist = artistService.findById(id);
        return ResponseEntity.ok(artist);
    }

    @PatchMapping("/artist/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(
            @PathVariable UUID id,
            @RequestBody io.student.rococo.dto.ArtistPatchDTO patch) {
        var updated = artistService.update(id, patch);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/artist")
    public ResponseEntity<ArtistDTO> updateArtistList(
            Pageable pageable,
            @RequestParam(required = false) String name) {
        List<ArtistDTO> result = artistService.getList(pageable, name).getContent();
        if (result.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        var first = result.get(0);
        return ResponseEntity.ok(first);
    }
}