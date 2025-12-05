package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.CreatePaymentMethodUseCase;
import edu.dosw.rideci.application.port.in.DeletePaymentMethodUseCase;
import edu.dosw.rideci.application.port.in.GetPaymentMethodsUseCase;
import edu.dosw.rideci.application.port.in.SetDefaultPaymentMethodUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreatePaymentMethodRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentMethodResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    private final GetPaymentMethodsUseCase getUseCase;

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
                PaymentMethodResponse.fromDomain(updated));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentMethodResponse>> getByUser(@PathVariable String userId) {

        var list = getUseCase.getByUserId(userId).stream()
                .map(PaymentMethodResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> getById(@PathVariable String id) {

        var pm = getUseCase.getById(id);

        return ResponseEntity.ok(PaymentMethodResponse.fromDomain(pm));
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> findAll() {

        var list = getUseCase.findAll().stream()
                .map(PaymentMethodResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(list);
    }

    /** * Eliminar método de pago */ 
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id) { 
        boolean deleted = deleteUseCase.delete(id); 
        if (deleted) { return ResponseEntity.noContent().build(); 

        } else { 
            return ResponseEntity.notFound().build(); }
    }


}
