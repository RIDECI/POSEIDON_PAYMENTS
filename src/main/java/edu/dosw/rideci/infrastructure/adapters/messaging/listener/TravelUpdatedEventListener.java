package edu.dosw.rideci.infrastructure.adapters.messaging.listener;

import edu.dosw.rideci.application.port.in.CompleteTripPaymentsUseCase;
import edu.dosw.rideci.infrastructure.adapters.messaging.dto.TravelUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelUpdatedEventListener {

    private final CompleteTripPaymentsUseCase completeTripPaymentsUseCase;

    @RabbitListener(queues = "geolocation.travel.updated.queue")
    public void handleTravelUpdated(TravelUpdatedEvent event) {
        log.info("🔄 TRAVEL UPDATED EVENT RECEIVED in PAYMENTS module");
        log.info("Travel ID: {}", event.getTravelId());
        log.info("Status: {}", event.getStatus());

        try {
            // Verificar si el viaje está COMPLETADO
            if ("COMPLETED".equalsIgnoreCase(event.getStatus())) {
                log.info("✅ TRAVEL COMPLETED - Processing payment");
                log.info("User ID: {}", event.getUserId());
                log.info("Driver ID: {}", event.getDriverId());
                log.info("Final Fare: ${}", event.getFinalFare());
                
                // Procesar el pago: cobrar al pasajero y pagar al conductor
                completeTripPaymentsUseCase.completeTripPayments(event.getTravelId());
                
                log.info("💰 Payment processed successfully for travel: {}", event.getTravelId());
                
            } else {
                // Solo registrar otras actualizaciones del viaje
                log.info("ℹ️ Travel status: {} - No payment action needed", event.getStatus());
            }
            
        } catch (Exception ex) {
            log.error("❌ Failed to process payment for travelId={}", event.getTravelId(), ex);
        }
    }
}
