package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.infrastructure.controller.dto.Request.PaymentSuspensionRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentSuspensionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/payments/suspensions")
@RequiredArgsConstructor
public class AdminPaymentSuspensionController {

    private final CreateSuspensionUseCase createSuspensionUseCase;
    private final GetSuspensionUseCase getSuspensionUseCase;
    private final UpdateSuspensionUseCase updateSuspensionUseCase;
    private final RevokeSuspensionUseCase revokeSuspensionUseCase;
    private final DeleteSuspensionUseCase deleteSuspensionUseCase;

    @PostMapping
    public ResponseEntity<PaymentSuspensionResponse> create(@RequestBody PaymentSuspensionRequest req) {
        PaymentSuspension s = PaymentSuspension.builder()
                .transactionId(req.getTransactionId())
                .reason(req.getReason())
                .expiresAt(req.getExpiresAt())
                .adminId(req.getAdminId())
                .build();

        PaymentSuspension created = createSuspensionUseCase.create(s);
        return ResponseEntity.ok(PaymentSuspensionResponse.fromDomain(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentSuspensionResponse> getById(@PathVariable String id) {
        return getSuspensionUseCase.getById(id)
                .map(s -> ResponseEntity.ok(PaymentSuspensionResponse.fromDomain(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaymentSuspensionResponse>> getAll() {
        var list = getSuspensionUseCase.getAll().stream()
                .map(PaymentSuspensionResponse::fromDomain).toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentSuspensionResponse> update(
            @PathVariable String id,
            @RequestBody PaymentSuspensionRequest req
    ) {
        PaymentSuspension updated = updateSuspensionUseCase.update(id, req.getReason(), req.getExpiresAt(), req.getAdminId());
        return ResponseEntity.ok(PaymentSuspensionResponse.fromDomain(updated));
    }

    @PatchMapping("/{id}/revoke")
    public ResponseEntity<PaymentSuspensionResponse> revoke(
            @PathVariable String id,
            @RequestParam(required = false) String adminId
    ) {
        PaymentSuspension revoked = revokeSuspensionUseCase.revoke(id, adminId);
        return ResponseEntity.ok(PaymentSuspensionResponse.fromDomain(revoked));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = deleteSuspensionUseCase.deleteById(id);
        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
