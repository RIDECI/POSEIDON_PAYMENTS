package edu.dosw.rideci.infrastructure.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.rideci.application.port.in.CreateCreditCardUseCase;
import edu.dosw.rideci.application.port.in.DeleteCreditCardUseCase;
import edu.dosw.rideci.application.port.in.GetCreditCardUseCase;
import edu.dosw.rideci.application.port.in.SetDefaultCreditCardUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateCreditCardRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.CreditCardResponse;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreateCreditCardUseCase createUseCase;
    private final GetCreditCardUseCase getUseCase;
    private final DeleteCreditCardUseCase deleteUseCase;
    private final SetDefaultCreditCardUseCase setDefaultUseCase;

    // Crear tarjeta
    @PostMapping
    public ResponseEntity<CreditCardResponse> create(
            @Valid @RequestBody CreateCreditCardRequest req) {

        var created = createUseCase.create(req.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreditCardResponse.fromDomain(created));
    }

    // Obtener todas las tarjetas (findAll)
    @GetMapping
    public ResponseEntity<List<CreditCardResponse>> findAll() {
        var cards = getUseCase.findAll().stream()
                .map(CreditCardResponse::fromDomain).toList();
        return ResponseEntity.ok(cards);
    }

    // Obtener todas las tarjetas de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CreditCardResponse>> findByUser(@PathVariable String userId) {
        var cards = getUseCase.findByUser(userId).stream()
                .map(CreditCardResponse::fromDomain).toList();
        return ResponseEntity.ok(cards);
    }

    // Obtener una tarjeta por ID
    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponse> findById(@PathVariable String id) {
        var card = getUseCase.findById(id);
        return ResponseEntity.ok(CreditCardResponse.fromDomain(card));
    }

    // Establecer tarjeta predeterminada
    @PatchMapping("/{id}/default")
    public ResponseEntity<CreditCardResponse> setDefault(@PathVariable String id) {
        var updated = setDefaultUseCase.setDefault(id);
        return ResponseEntity.ok(CreditCardResponse.fromDomain(updated));
    }

    // Eliminar tarjeta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = deleteUseCase.delete(id);

        if (deleted) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
