package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.ProcessWebhookUseCase;
import edu.dosw.rideci.application.port.in.UpdatePaymentByWebhookUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.controller.dto.Request.WebhookPaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.WebhookUpdateRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/payments/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final ProcessWebhookUseCase processWebhookUseCase;
    private final UpdatePaymentByWebhookUseCase updatePaymentByWebhookUseCase;

    @PostMapping("/{provider}")
    public ResponseEntity<TransactionResponse> handleWebhook(
            @PathVariable String provider,
            @RequestBody WebhookPaymentRequest request) {
        log.info("Received webhook from provider: {} for transaction: {}", 
                 provider, request.getTransactionId());
        
        try {
            Transaction processedPayment = processWebhookUseCase.processWebhook(
                    provider,
                    request.getTransactionId(),
                    request.getExternalReference(),
                    request.getStatus(),
                    request.getAmount(),
                    request.getPaymentMethod(),
                    request.getMetadata()
            );
            
            log.info("Webhook processed successfully from provider: {}", provider);
            return ResponseEntity.ok(TransactionResponse.fromDomain(processedPayment));
            
        } catch (Exception e) {
            log.error("Error processing webhook from provider {}: {}", provider, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{provider}/{paymentId}")
    public ResponseEntity<TransactionResponse> updatePaymentStatus(
            @PathVariable String provider,
            @PathVariable String paymentId,
            @RequestBody WebhookUpdateRequest request) {
        log.info("Received webhook update from provider: {} for payment: {}", 
                 provider, paymentId);
        
        try {
            Transaction updatedPayment = updatePaymentByWebhookUseCase.updateByWebhook(
                    provider,
                    paymentId,
                    request.getStatus(),
                    request.getReceiptCode(),
                    request.getErrorMessage(),
                    request.getMetadata()
            );
            
            log.info("Payment {} updated successfully via webhook from provider: {}", 
                     paymentId, provider);
            return ResponseEntity.ok(TransactionResponse.fromDomain(updatedPayment));
            
        } catch (Exception e) {
            log.error("Error updating payment via webhook from provider {}: {}", 
                     provider, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
