package io.student.rococo.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtistPatchDTO {
    private UUID id;
    private String name;
    private String biography;
    private String photo;
}