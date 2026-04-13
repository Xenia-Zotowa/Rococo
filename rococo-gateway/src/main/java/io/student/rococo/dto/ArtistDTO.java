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
public class ArtistDTO {
    @NotNull(message = "ID cannot be null")
    private UUID id;
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name is too long")
    private String name;
    @Size(max = 1000, message = "Biography is too long")
    private String biography;
    private String photo;
}