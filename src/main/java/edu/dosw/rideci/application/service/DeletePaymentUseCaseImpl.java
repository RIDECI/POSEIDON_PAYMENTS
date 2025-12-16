package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.DeletePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePaymentUseCaseImpl implements DeletePaymentUseCase {

    private final PaymentRepositoryPort repository;

    @Override
    public boolean deleteById(String id) {

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
