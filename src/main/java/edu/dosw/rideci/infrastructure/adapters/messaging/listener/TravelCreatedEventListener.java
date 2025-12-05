package edu.dosw.rideci.infrastructure.adapters.messaging.listener;

import edu.dosw.rideci.infrastructure.adapters.messaging.dto.TravelCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelCreatedEventListener {

    @RabbitListener(queues = "geolocation.travel.created.queue")
    public void handleTravelCreated(TravelCreatedEvent event) {
        log.info("TRAVEL CREATED EVENT RECEIVED in PAYMENTS module");
        log.info("Travel ID: {}", event.getTravelId());
        log.info("User ID: {}", event.getUserId());
        log.info("Driver ID: {}", event.getDriverId());
        log.info("Estimated Fare: {}", event.getEstimatedFare());
        log.info("Origin: {} ({}, {})", 
            event.getOrigin().getDirection(), 
            event.getOrigin().getLatitude(), 
            event.getOrigin().getLongitude());
        log.info("Destiny: {} ({}, {})", 
            event.getDestiny().getDirection(), 
            event.getDestiny().getLatitude(), 
            event.getDestiny().getLongitude());

        try {
            // Crear un pago pendiente cuando se crea el viaje
            // El pasajero (userId) pagará al sistema
            log.info("💳 Travel created - Payment will be processed when travel completes");
            
            // TODO: Implementar lógica si se necesita crear un pago anticipado
            // Por ahora, el pago se procesará cuando el viaje se complete (TravelCompletedEvent)
            // Si necesitas crear un pago PENDING aquí, descomenta y ajusta:
            // createPaymentUseCase.createPayment(...);
            
            log.info("✅ Travel event processed for travel: {}", event.getTravelId());
            
        } catch (Exception ex) {
            log.error("❌ Failed to create payment for travelId={}. Event will be logged for manual review.",
                    event.getTravelId(), ex);
        }
    }
}
