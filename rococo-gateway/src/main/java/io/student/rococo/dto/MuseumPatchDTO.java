package io.student.rococo.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuseumPatchDTO {
    private UUID id;
    private String title;
    private String description;
    private String photo;
    @Valid
    private GeoDTO geo;
}