package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreatePaymentUseCase;
import edu.dosw.rideci.application.port.in.DeletePaymentUseCase;
import edu.dosw.rideci.application.port.in.ProcessPaymentUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentUseCase;
import edu.dosw.rideci.application.port.in.UpdatePaymentUseCase;
import edu.dosw.rideci.application.port.in.AuthorizePaymentUseCase;
import edu.dosw.rideci.application.port.in.ApprovePaymentUseCase;
import edu.dosw.rideci.application.port.in.CompletePaymentUseCase;
import edu.dosw.rideci.application.port.in.CancelPaymentUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentStatusUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentsByTripUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentsByUserUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentsByDateUseCase;
import edu.dosw.rideci.application.port.in.GetActivePaymentsUseCase;
import edu.dosw.rideci.application.port.in.GetTransactionsByPaymentMethodUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreatePaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.UpdatePaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentUseCase getPaymentUseCase;
    private final DeletePaymentUseCase deletePaymentUseCase;
    private final UpdatePaymentUseCase updatePaymentUseCase;
    private final AuthorizePaymentUseCase authorizePaymentUseCase;
    private final ApprovePaymentUseCase approvePaymentUseCase;
    private final CompletePaymentUseCase completePaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final GetPaymentStatusUseCase getPaymentStatusUseCase;
    private final GetPaymentsByTripUseCase getPaymentsByTripUseCase;
    private final GetPaymentsByUserUseCase getPaymentsByUserUseCase;
    private final GetPaymentsByDateUseCase getPaymentsByDateUseCase;
    private final GetActivePaymentsUseCase getActivePaymentsUseCase;
    private final GetTransactionsByPaymentMethodUseCase getTransactionsByPaymentMethodUseCase;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> create(@RequestBody CreatePaymentRequest request) {
        Transaction tx = createPaymentUseCase.create(request.toDomain());
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable String id) {
        return getPaymentUseCase.getById(id)
                .map(tx -> ResponseEntity.ok(TransactionResponse.fromDomain(tx)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(@PathVariable String id) {
        return getPaymentUseCase.getById(id)
                .map(tx -> ResponseEntity.ok(PaymentStatusResponse.fromDomain(tx)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<List<TransactionResponse>> getPaymentsByTrip(@PathVariable String id) {
        List<Transaction> payments = getPaymentsByTripUseCase.getByTripId(id);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponse> response = payments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TransactionResponse>> getPaymentsByUser(@PathVariable String id) {
        List<Transaction> payments = getPaymentsByUserUseCase.getByUserId(id);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponse> response = payments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> findAll() {
        List<Transaction> txList = getPaymentUseCase.findAll();

        if (txList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponse> response = txList.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{date}")
    public ResponseEntity<List<TransactionResponse>> getPaymentsByDate(@PathVariable String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        List<Transaction> payments = getPaymentsByDateUseCase.getByDate(localDate);

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponse> response = payments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<TransactionResponse>> getActivePayments() {
        List<Transaction> payments = getActivePaymentsUseCase.getActivePayments();

        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TransactionResponse> response = payments.stream()
                .map(TransactionResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String id) {

        boolean deleted = deletePaymentUseCase.deleteById(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponse> updatePayment(
            @PathVariable String id,
            @RequestBody UpdatePaymentRequest request) {
        Transaction updated = updatePaymentUseCase.updatePartial(id, request);
        return ResponseEntity.ok(TransactionResponse.fromDomain(updated));
    }

    @PatchMapping("/{id}/authorize")
    public ResponseEntity<TransactionResponse> authorizePayment(
            @PathVariable String id,
            @RequestParam boolean authorized) {
        Transaction tx = authorizePaymentUseCase.authorize(id, authorized);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<TransactionResponse> processPayment(
            @PathVariable String id) {
        Transaction tx = processPaymentUseCase.process(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<TransactionResponse> approvePayment(
            @PathVariable String id) {
        Transaction tx = approvePaymentUseCase.approve(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TransactionResponse> completePayment(
            @PathVariable String id) {
        Transaction tx = completePaymentUseCase.complete(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TransactionResponse> cancelPayment(@PathVariable String id) {
        Transaction tx = cancelPaymentUseCase.cancel(id);
        return ResponseEntity.ok(TransactionResponse.fromDomain(tx));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponse>> getByStatus(@PathVariable String status) {
        TransactionStatus st;

        try {
            st = TransactionStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<Transaction> list = getPaymentStatusUseCase.findByStatus(st);

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                list.stream().map(TransactionResponse::fromDomain).toList());
    }

    @GetMapping("/method/{paymentMethodType}")
    public List<Transaction> getTransactionsByPaymentMethod(@PathVariable PaymentMethodType paymentMethodType) {
        return getTransactionsByPaymentMethodUseCase.findByPaymentMethod(paymentMethodType);
    }

}
