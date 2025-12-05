package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.DeleteBrebKeyUseCase;
import edu.dosw.rideci.application.port.out.BrebKeyRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteBrebKeyUseCaseImpl implements DeleteBrebKeyUseCase {

    private final BrebKeyRepositoryPort repo;

    @Override
    public boolean delete(String id) {
        if (repo.findById(id).isEmpty()) {
            return false;
        }

        repo.deleteById(id);
        return true;
    }
}
