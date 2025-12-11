package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.GeneratePaymentReceiptUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentReceiptUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.GenerateReceiptRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentReceiptResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments/receipt")
@RequiredArgsConstructor
public class PaymentReceiptController {

    private final GeneratePaymentReceiptUseCase generateReceiptUseCase;
    private final GetPaymentReceiptUseCase getReceiptUseCase;

    @PatchMapping("/{transactionId}")
    public ResponseEntity<PaymentReceiptResponse> generate(
            @PathVariable String transactionId,
            @RequestBody GenerateReceiptRequest request
    ) {

        var receipt = generateReceiptUseCase.generate(
                transactionId,
                request.getDriverId()
        );

        return ResponseEntity.ok(PaymentReceiptResponse.fromDomain(receipt));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PaymentReceiptResponse> getById(@PathVariable String id) {

        var receipt = getReceiptUseCase.getById(id);
        return ResponseEntity.ok(PaymentReceiptResponse.fromDomain(receipt));
    }


    @GetMapping("/code/{receiptCode}")
    public ResponseEntity<PaymentReceiptResponse> getByCode(@PathVariable String receiptCode) {

        var receipt = getReceiptUseCase.getByReceiptCode(receiptCode);
        return ResponseEntity.ok(PaymentReceiptResponse.fromDomain(receipt));
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentReceiptResponse> getByTransaction(
            @PathVariable String transactionId
    ) {

        var receipt = getReceiptUseCase.getByTransactionId(transactionId);
        return ResponseEntity.ok(PaymentReceiptResponse.fromDomain(receipt));
    }

    @GetMapping
    public ResponseEntity<List<PaymentReceiptResponse>> getAll() {

        var list = getReceiptUseCase.getAll().stream()
                .map(PaymentReceiptResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(list);
    }
}
