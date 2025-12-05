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

import edu.dosw.rideci.application.port.in.CreateBrebKeyUseCase;
import edu.dosw.rideci.application.port.in.DeleteBrebKeyUseCase;
import edu.dosw.rideci.application.port.in.GetBrebKeyUseCase;
import edu.dosw.rideci.application.port.in.SetDefaultBrebKeyUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreateBrebKeyRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.BrebKeyResponse;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/breb-keys")
@RequiredArgsConstructor
public class BrebKeyController {

    private final CreateBrebKeyUseCase createUseCase;
    private final GetBrebKeyUseCase getUseCase;
    private final DeleteBrebKeyUseCase deleteUseCase;
    private final SetDefaultBrebKeyUseCase setDefaultUseCase;

    @PostMapping
    public ResponseEntity<BrebKeyResponse> create(
            @Valid @RequestBody CreateBrebKeyRequest req) {

        var created = createUseCase.create(req.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BrebKeyResponse.fromDomain(created));
    }

    @GetMapping
    public ResponseEntity<List<BrebKeyResponse>> findAll() {
        var list = getUseCase.findAll()
                .stream()
                .map(BrebKeyResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BrebKeyResponse>> findByUser(@PathVariable String userId) {
        var list = getUseCase.findByUser(userId)
                .stream()
                .map(BrebKeyResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<BrebKeyResponse> setDefault(@PathVariable String id) {
        var updated = setDefaultUseCase.setDefault(id);
        return ResponseEntity.ok(BrebKeyResponse.fromDomain(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        boolean deleted = deleteUseCase.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
