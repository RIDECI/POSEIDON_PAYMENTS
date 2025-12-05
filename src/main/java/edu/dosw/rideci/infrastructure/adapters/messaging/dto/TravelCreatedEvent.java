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
public class TravelCreatedEvent {
    private String travelId;
    private String userId;
    private String driverId;
    private LocationDto origin;
    private LocationDto destiny;
    private LocalDateTime departureDateAndTime;
    private Double estimatedFare;
    private String status;
}
