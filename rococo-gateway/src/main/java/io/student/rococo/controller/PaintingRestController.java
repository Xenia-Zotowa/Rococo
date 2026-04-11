package io.student.rococo.controller;

import io.student.rococo.dto.PageableResponse;
import io.student.rococo.service.PaintingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Pageable;
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
        return ResponseEntity.ok(paintingService.getList(pageable, title, museumId));
    }

    @GetMapping("/painting/{id}")
    public ResponseEntity<io.student.rococo.dto.PaintingDTO> getPainting(@PathVariable UUID id) {
        var painting = paintingService.findById(id);
        return ResponseEntity.ok(painting);
    }

    @GetMapping("/painting/author/{artistId}")
    public ResponseEntity<PageableResponse<io.student.rococo.dto.PaintingDTO>> getPaintingsByArtist(@PathVariable UUID artistId) {
        var paintings = paintingService.findByArtistId(artistId);
        // Since findByArtistId returns List, we wrap it in a simple PageableResponse to match frontend expectation
        return ResponseEntity.ok(new io.student.rococo.dto.PageableResponse<>(
                paintings, 0, 1, paintings.size(), 1, true, true));
    }

    @PostMapping("/painting")
    public ResponseEntity<io.student.rococo.dto.PaintingDTO> createPainting(@RequestBody io.student.rococo.dto.PaintingDTO dto) {
        var painting = paintingService.save(dto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(painting);
    }

    @PatchMapping("/painting")
    public ResponseEntity<io.student.rococo.dto.PaintingDTO> updatePainting(
            @RequestBody io.student.rococo.dto.PaintingPatchDTO patch) {
        if (patch.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        var updated = paintingService.updatePainting(patch.getId(), patch);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/error")
    public String handleError() {
        return "redirect:/api";
    }
}