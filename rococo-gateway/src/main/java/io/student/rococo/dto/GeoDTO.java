package io.student.rococo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeoDTO {
    private String city;
    private CountryDTO country;
    private Double latitude;
    private Double longitude;
}