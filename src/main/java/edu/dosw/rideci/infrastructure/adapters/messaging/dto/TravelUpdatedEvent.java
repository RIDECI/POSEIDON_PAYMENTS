package edu.dosw.rideci.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelUpdatedEvent {
    private String travelId;
    private String userId;
    private String driverId;
    private LocationDto currentLocation;
    private String status;
    private LocalDateTime updatedAt;
    private Double finalFare; // Tarifa final cuando el viaje se completa
}
