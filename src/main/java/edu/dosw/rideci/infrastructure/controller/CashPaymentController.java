package edu.dosw.rideci.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.rideci.application.port.in.CashPaymentQueryUseCase;
import edu.dosw.rideci.application.port.in.ConfirmCashPaymentUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CashPaymentConfirmationRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.CashPaymentConfirmationResponse;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments/cash")
@RequiredArgsConstructor
public class CashPaymentController {

    private final ConfirmCashPaymentUseCase confirmUseCase;
    private final CashPaymentQueryUseCase queryUseCase;

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<CashPaymentConfirmationResponse> confirm(
            @PathVariable String id,
            @RequestBody CashPaymentConfirmationRequest req) {
        var confirmation = confirmUseCase.confirm(id, req.getDriverId(), req.getObservations());
        return ResponseEntity.ok(CashPaymentConfirmationResponse.fromDomain(confirmation));
    }

    @GetMapping("/confirmations/{id}")
    public ResponseEntity<CashPaymentConfirmationResponse> getById(@PathVariable String id) {
        var result = queryUseCase.getById(id);
        return ResponseEntity.ok(CashPaymentConfirmationResponse.fromDomain(result));
    }

    @GetMapping("/transactions/{transactionId}/confirmation")
    public ResponseEntity<CashPaymentConfirmationResponse> getByTransactionId(
            @PathVariable String transactionId) {

        var result = queryUseCase.getByTransactionId(transactionId);
        return ResponseEntity.ok(CashPaymentConfirmationResponse.fromDomain(result));
    }

    @GetMapping("/confirmations")
    public ResponseEntity<List<CashPaymentConfirmationResponse>> getAll() {
        var result = queryUseCase.getAll()
                .stream()
                .map(CashPaymentConfirmationResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(result);
    }

}
