package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetBrebKeyUseCase;
import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.domain.model.BrebKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetBrebKeyUseCaseImpl implements GetBrebKeyUseCase {

    private final BrebKeyRepositoryPort repo;

    @Override
    public List<BrebKey> findAll() {
        return repo.findAll();
    }

    @Override
    public List<BrebKey> findByUser(String userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public BrebKey findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Llave BreB no encontrada"));
    }

    @Override
    public BrebKey findDefault(String userId) {
        return repo.findDefaultForUser(userId)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene llave BREB predeterminada"));
    }
}
