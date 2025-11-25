package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.RefundPaymentUseCase;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.application.port.in.CancelRefundUseCase;
import edu.dosw.rideci.application.port.in.DeleteRefundUseCase;
import edu.dosw.rideci.application.port.in.GetRefundUseCase;
import edu.dosw.rideci.application.port.in.GetRefundStatusUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.RefundRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.RefundResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class RefundController {

    private final RefundPaymentUseCase refundPaymentUseCase;
    private final CancelRefundUseCase cancelRefundUseCase;
    private final DeleteRefundUseCase deleteRefundUseCase;
    private final GetRefundUseCase getRefundUseCase;
    private final GetRefundStatusUseCase getRefundStatusUseCase;

    @PatchMapping("/{id}/refund")
    public ResponseEntity<RefundResponse> refund(
            @PathVariable String id,
            @RequestBody RefundRequest request) {
        var refund = refundPaymentUseCase.refundPayment(
                id,
                request.getAmount(),
                request.getReason());

        return ResponseEntity.ok(RefundResponse.fromDomain(refund));
    }

    @PatchMapping("/{id}/refund/cancel")
    public ResponseEntity<RefundResponse> cancelRefund(@PathVariable String id) {
        var refund = cancelRefundUseCase.cancel(id);
        return ResponseEntity.ok(RefundResponse.fromDomain(refund));
    }

    @DeleteMapping("/{id}/refund")
    public ResponseEntity<Void> deleteRefund(@PathVariable String id) {

        boolean deleted = deleteRefundUseCase.deleteById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/refund")
    public ResponseEntity<RefundResponse> getRefundById(@PathVariable String id) {
        return getRefundUseCase.getById(id)
                .map(refund -> ResponseEntity.ok(RefundResponse.fromDomain(refund)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/refund")
    public ResponseEntity<List<RefundResponse>> getRefundHistory() {
        var list = getRefundUseCase.getAll()
                .stream()
                .map(RefundResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/statusR/{status}")
    public ResponseEntity<List<RefundResponse>> getByStatus(@PathVariable String status) {

        RefundStatus st;
        try {
            st = RefundStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        var list = getRefundStatusUseCase.findByStatus(st);

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                list.stream().map(RefundResponse::fromDomain).toList());
    }

}
