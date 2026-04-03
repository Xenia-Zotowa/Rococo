package io.student.rococo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuseumDTO {
    private UUID id;
    private String title;
    private String description;
    private byte[] photo;
    private GeoDTO geo;
}