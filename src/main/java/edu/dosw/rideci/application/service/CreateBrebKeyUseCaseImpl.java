package edu.dosw.rideci.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CreateBrebKeyUseCase;
import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.domain.model.BrebKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateBrebKeyUseCaseImpl implements CreateBrebKeyUseCase {

    private final BrebKeyRepositoryPort repo;

    @Override
    public BrebKey create(BrebKey key) {

        key.setId("BREB-" + UUID.randomUUID());

        key.setDefault(false);

        return repo.save(key);
    }
}
