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
public class TravelCompletedEvent {
    private String travelId;
    private String userId;
    private String driverId;
    private Double finalFare;
    private Double distance;
    private LocalDateTime completedAt;
    private String status;
}
