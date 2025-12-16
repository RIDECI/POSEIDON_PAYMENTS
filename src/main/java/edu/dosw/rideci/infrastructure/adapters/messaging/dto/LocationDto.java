package edu.dosw.rideci.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private String direction;
    private Double latitude;
    private Double longitude;
}
