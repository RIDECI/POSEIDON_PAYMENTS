package edu.dosw.rideci.infrastructure.adapters.messaging.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.dosw.rideci.infrastructure.adapters.messaging.dto.PaymentEvent;
import edu.dosw.rideci.infrastructure.config.RabbitMQPaymentsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publishPaymentCompleted(String paymentId, String userId, String driverId, String tripId, Double amount) {
        try {
            PaymentEvent event = PaymentEvent.builder()
                    .paymentId(paymentId)
                    .userId(userId)
                    .driverId(driverId)
                    .tripId(tripId)
                    .status("COMPLETED")
                    .amount(amount)
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            String message = objectMapper.writeValueAsString(event);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQPaymentsConfig.PAYMENT_EXCHANGE,
                    RabbitMQPaymentsConfig.PAYMENT_COMPLETED_ROUTING_KEY,
                    message
            );
            
            log.info("Published PAYMENT COMPLETED event: {} - User {} paid ${} to Driver {}", 
                paymentId, userId, amount, driverId);
            
        } catch (Exception e) {
            log.error("Error publishing payment completed event: {}", e.getMessage(), e);
        }
    }

    public void publishPaymentFailed(String paymentId, String userId, String driverId, String tripId, String reason) {
        try {
            PaymentEvent event = PaymentEvent.builder()
                    .paymentId(paymentId)
                    .userId(userId)
                    .driverId(driverId)
                    .tripId(tripId)
                    .status("FAILED")
                    .amount(0.0)
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            String message = objectMapper.writeValueAsString(event);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQPaymentsConfig.PAYMENT_EXCHANGE,
                    RabbitMQPaymentsConfig.PAYMENT_FAILED_ROUTING_KEY,
                    message
            );
            
            log.info("Published PAYMENT FAILED event: {}", paymentId);
            
        } catch (Exception e) {
            log.error("Error publishing payment failed event: {}", e.getMessage(), e);
        }
    }

    public void publishRefundCompleted(String paymentId, String userId, String driverId, String tripId, Double amount) {
        try {
            PaymentEvent event = PaymentEvent.builder()
                    .paymentId(paymentId)
                    .userId(userId)
                    .driverId(driverId)
                    .tripId(tripId)
                    .status("REFUNDED")
                    .amount(amount)
                    .timestamp(LocalDateTime.now().toString())
                    .build();

            String message = objectMapper.writeValueAsString(event);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQPaymentsConfig.PAYMENT_EXCHANGE,
                    RabbitMQPaymentsConfig.REFUND_COMPLETED_ROUTING_KEY,
                    message
            );
            
            log.info("Published REFUND COMPLETED event: {}", paymentId);
            
        } catch (Exception e) {
            log.error("Error publishing refund completed event: {}", e.getMessage(), e);
        }
    }
}
