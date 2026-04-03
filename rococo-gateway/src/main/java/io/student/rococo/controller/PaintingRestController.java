package io.student.rococo.controller;

import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.PaintingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PaintingRestController {
    private final PaintingService paintingService;

    public PaintingRestController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping("/painting")
    public ResponseEntity<PageableResponse<io.student.rococo.dto.PaintingDTO>> getPaintings(
            Pageable pageable,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long museumId) {
        return ResponseEntity.ok(paintingService.getList(pageable));
    }

    @GetMapping("/painting/{id}")
    public ResponseEntity<io.student.rococo.dto.PaintingDTO> getPainting(@PathVariable UUID id) {
        var painting = paintingService.findById(id);
        return ResponseEntity.ok(painting);
    }

    @GetMapping("/painting/by-artist/{artistId}")
    public ResponseEntity<List<io.student.rococo.dto.PaintingDTO>> getPaintingsByArtist(@PathVariable UUID artistId) {
        var paintings = paintingService.findByArtistId(artistId);
        return ResponseEntity.ok(paintings);
    }

    @PatchMapping("/painting/{id}")
    public ResponseEntity<io.student.rococo.dto.PaintingDTO> updatePainting(
            @PathVariable UUID id,
            @RequestBody io.student.rococo.dto.PaintingPatchDTO patch) {
        var painting = paintingService.findById(id);
        if (patch == null) return ResponseEntity.ok(painting);
        if (patch.getTitle() != null) painting.setTitle(patch.getTitle());
        if (patch.getDescription() != null) painting.setDescription(patch.getDescription());
        if (patch.getContent() != null) painting.setContent(patch.getContent());
        var updated = paintingService.updatePainting(painting);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/error")
    public String handleError() {
        return "redirect:/api";
    }
}