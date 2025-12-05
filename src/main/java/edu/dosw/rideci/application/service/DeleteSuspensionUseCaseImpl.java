package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.DeleteSuspensionUseCase;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSuspensionUseCaseImpl implements DeleteSuspensionUseCase {

    private final PaymentSuspensionRepositoryPort repository;

    @Override
    public boolean deleteById(String id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
