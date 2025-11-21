package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreatePaymentUseCase;
import edu.dosw.rideci.application.port.in.ProcessPaymentUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreatePaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> create(@RequestBody CreatePaymentRequest request) {
        Transaction tx = createPaymentUseCase.create(request.toDomain());
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<TransactionResponse> process(@PathVariable String id) {
        Transaction tx = processPaymentUseCase.process(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable String id) {
        // For correctness add a GetPaymentUseCase; here quick demo:
        var opt = createPaymentUseCase; // placeholder to signal you should add GetPaymentUseCase
        // Implement GetPaymentUseCase in application.port.in and the service to return real data.
        return ResponseEntity.notFound().build();
    }
}
