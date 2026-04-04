package io.student.rococo.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuseumPatchDTO {
    private String title;
    private String description;
    private byte[] photo;
    @Valid
    private GeoDTO geo;
}