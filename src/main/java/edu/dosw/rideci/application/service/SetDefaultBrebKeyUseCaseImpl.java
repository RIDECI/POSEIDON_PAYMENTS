package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.SetDefaultBrebKeyUseCase;
import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import edu.dosw.rideci.domain.model.BrebKey;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetDefaultBrebKeyUseCaseImpl implements SetDefaultBrebKeyUseCase {

    private final BrebKeyRepositoryPort repo;

    @Override
    public BrebKey setDefault(String id) {

        var key = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Llave BreB no encontrada"));

        
        repo.clearDefaults(key.getUserId());

        
        key.setDefault(true);
        return repo.save(key);
    }
}
