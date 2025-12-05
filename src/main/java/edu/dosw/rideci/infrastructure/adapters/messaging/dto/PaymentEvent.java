package edu.dosw.rideci.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    private String paymentId;
    private String userId; // Quien hizo el pago (pasajero)
    private String driverId; // Quien recibió el pago (conductor)
    private String tripId;
    private String status; // "COMPLETED", "FAILED", "REFUNDED"
    private Double amount; // Total del monto
    private String timestamp;
}
