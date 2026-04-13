package io.student.rococo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaintingPatchDTO {
    private String title;
    private String description;
    private String content;
    private Long museumId;
    private java.util.UUID id;
}