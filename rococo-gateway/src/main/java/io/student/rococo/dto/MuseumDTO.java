package io.student.rococo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "ID cannot be null")
    private UUID id;
    @NotBlank(message =
            "Title cannot be blank")
    @Size(max = 255, message = "Title is too long")
    private String title;
    @Size(max = 1000, message = "Description is too long")
    private String description;
    private String photo;
    @NotNull(message = "Geo data cannot be null")
    private GeoDTO geo;
}