package edu.dosw.rideci.infrastructure.adapters.messaging.listener;

import edu.dosw.rideci.application.port.in.CancelTripPaymentsUseCase;
import edu.dosw.rideci.infrastructure.adapters.messaging.dto.TravelCancelledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelCancelledEventListener {

    private final CancelTripPaymentsUseCase cancelTripPaymentsUseCase;

    @RabbitListener(queues = "geolocation.travel.cancelled.queue")
    public void handleTravelCancelled(TravelCancelledEvent event) {
        log.info("TRAVEL CANCELLED EVENT RECEIVED in PAYMENTS module");
        log.info("Travel ID: {}", event.getTravelId());
        log.info("User ID: {}", event.getUserId());
        log.info("Driver ID: {}", event.getDriverId());
        log.info("Cancelled By: {}", event.getCancelledBy());
        log.info("Cancellation Reason: {}", event.getCancellationReason());
        log.info("Cancelled At: {}", event.getCancelledAt());

        try {
            log.info("🔄 Processing payment cancellation/refund for travel: {}", event.getTravelId());
            
            // Cancelar o reembolsar el pago del viaje
            // Esto debería:
            // 1. Cancelar el pago si aún no se ha procesado
            // 2. Iniciar un reembolso si el pago ya se procesó
            // 3. Aplicar penalización si corresponde (según quién canceló)
            // 4. Enviar notificaciones
            cancelTripPaymentsUseCase.cancelTripPayments(event.getTravelId());
            
            log.info("✅ Payment cancelled/refunded successfully for travel: {}", event.getTravelId());
            
            if ("PASSENGER".equalsIgnoreCase(event.getCancelledBy())) {
                log.info("⚠️ Cancellation by passenger - refund may be partial");
            } else if ("DRIVER".equalsIgnoreCase(event.getCancelledBy())) {
                log.info("⚠️ Cancellation by driver - full refund to passenger");
            }
            
        } catch (Exception ex) {
            log.error("❌ Failed to cancel/refund payment for travelId={}. Event will be logged for manual review.",
                    event.getTravelId(), ex);
            // TODO: Considerar enviar a una cola de reintentos o DLQ
        }
    }
}
