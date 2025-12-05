package edu.dosw.rideci.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String direction;
    private Double latitude;
    private Double longitude;
}
