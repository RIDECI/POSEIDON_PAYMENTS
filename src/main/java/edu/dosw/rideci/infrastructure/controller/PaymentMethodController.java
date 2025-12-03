package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreatePaymentMethodUseCase;
import edu.dosw.rideci.application.port.in.DeletePaymentMethodUseCase;
import edu.dosw.rideci.application.port.in.SetDefaultPaymentMethodUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreatePaymentMethodRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentMethodResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final CreatePaymentMethodUseCase createUseCase;
    private final DeletePaymentMethodUseCase deleteUseCase;
    private final SetDefaultPaymentMethodUseCase setDefaultUseCase;

    /**
     * Crear método de pago
     */
    @PostMapping
    public ResponseEntity<PaymentMethodResponse> create(
            @Valid @RequestBody CreatePaymentMethodRequest req) {

        var created = createUseCase.create(req.toDomain());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentMethodResponse.fromDomain(created));
    }

    /**
     * Marcar método como predeterminado
     */
    @PatchMapping("/{id}/default")
    public ResponseEntity<PaymentMethodResponse> setDefault(@PathVariable String id) {

        var updated = setDefaultUseCase.setDefault(id);

        return ResponseEntity.ok(
                PaymentMethodResponse.fromDomain(updated)
        );
    }

    /**
     * Eliminar método de pago
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = deleteUseCase.delete(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
