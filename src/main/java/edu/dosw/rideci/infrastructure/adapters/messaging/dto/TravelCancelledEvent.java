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
public class TravelCancelledEvent {
    private String travelId;
    private String userId;
    private String driverId;
    private String cancelledBy;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private String status;
}
