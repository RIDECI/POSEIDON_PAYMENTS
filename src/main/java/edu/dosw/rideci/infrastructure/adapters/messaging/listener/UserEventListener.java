package edu.dosw.rideci.infrastructure.adapters.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.adapters.messaging.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventListener {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "payment.user.events.queue")
    public void handleUserEvent(String message) {
        try {
            log.info("👤 USER EVENT RECEIVED in PAYMENTS module");
            
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            
            log.info("User ID: {}", event.getUserId());
            log.info("Name: {}", event.getName());
            log.info("Email: {}", event.getEmail());
            log.info("State: {}", event.getState());
            log.info("Role: {}", event.getRole());
            
            // Lógica según el estado del usuario
            if ("BLOCKED".equalsIgnoreCase(event.getState())) {
                log.warn("🚫 User {} is BLOCKED - Should suspend active payments", event.getUserId());
                // TODO: Implementar suspensión de pagos
                // suspendUserPaymentsUseCase.suspend(event.getUserId());
            } 
            else if ("DELETED".equalsIgnoreCase(event.getState())) {
                log.warn("🗑️ User {} is DELETED - Should cancel pending payments", event.getUserId());
                // TODO: Implementar cancelación de pagos pendientes
                // cancelUserPendingPaymentsUseCase.cancel(event.getUserId());
            }
            else if ("ACTIVE".equalsIgnoreCase(event.getState())) {
                log.info("✅ User {} is ACTIVE - Can process payments", event.getUserId());
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing user event: {}", e.getMessage(), e);
        }
    }
}
