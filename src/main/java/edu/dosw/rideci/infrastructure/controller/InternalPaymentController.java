package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CancelTripPaymentsUseCase;
import edu.dosw.rideci.application.port.in.CompleteTripPaymentsUseCase;
import edu.dosw.rideci.application.port.in.VerifyPaymentApprovalUseCase;
import edu.dosw.rideci.application.port.in.RegisterPaymentFailureUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.controller.dto.Request.PaymentFailureRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentVerificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payments/internal")
@RequiredArgsConstructor
public class InternalPaymentController {

    private final CancelTripPaymentsUseCase cancelTripPaymentsUseCase;
    private final CompleteTripPaymentsUseCase completeTripPaymentsUseCase;
    private final VerifyPaymentApprovalUseCase verifyPaymentApprovalUseCase;
    private final RegisterPaymentFailureUseCase registerPaymentFailureUseCase;

    @PatchMapping("/trips/{tripId}/cancel")
    public ResponseEntity<List<TransactionResponse>> cancelTripPayments(@PathVariable String tripId) {
        log.info("Received request to cancel payments for trip: {}", tripId);
        
        List<Transaction> cancelledPayments = cancelTripPaymentsUseCase.cancelTripPayments(tripId);
        
        if (cancelledPayments.isEmpty()) {
            log.info("No payments were cancelled for trip: {}", tripId);
            return ResponseEntity.noContent().build();
        }
        
        List<TransactionResponse> response = cancelledPayments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/trips/{tripId}/complete")
    public ResponseEntity<List<TransactionResponse>> completeTripPayments(@PathVariable String tripId) {
        log.info("Received request to complete payments for trip: {}", tripId);
        
        List<Transaction> completedPayments = completeTripPaymentsUseCase.completeTripPayments(tripId);
        
        if (completedPayments.isEmpty()) {
            log.info("No payments were completed for trip: {}", tripId);
            return ResponseEntity.noContent().build();
        }
        
        List<TransactionResponse> response = completedPayments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{paymentId}")
    public ResponseEntity<PaymentVerificationResponse> verifyPaymentApproval(@PathVariable String paymentId) {
        log.info("Received request to verify payment approval: {}", paymentId);
        
        Transaction payment = verifyPaymentApprovalUseCase.verifyApproval(paymentId)
                .orElse(null);
        
        PaymentVerificationResponse response = PaymentVerificationResponse.fromDomain(payment);
        
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/failures")
    public ResponseEntity<TransactionResponse> registerPaymentFailure(
            @RequestBody PaymentFailureRequest request) {
        log.info("Received request to register payment failure for: {}", request.getPaymentId());
        
        Transaction updatedPayment = registerPaymentFailureUseCase.registerFailure(
                request.getPaymentId(),
                request.getErrorMessage(),
                request.getErrorCode()
        );
        
        return ResponseEntity.ok(TransactionResponse.fromDomain(updatedPayment));
    }
}
