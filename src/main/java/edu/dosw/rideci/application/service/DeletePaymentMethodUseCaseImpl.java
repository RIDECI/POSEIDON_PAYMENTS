package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.DeletePaymentMethodUseCase;
import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeletePaymentMethodUseCaseImpl implements DeletePaymentMethodUseCase {

    private final PaymentMethodRepositoryPort repo;

    @Override
    public boolean delete(String id) {
        if (repo.findById(id).isEmpty()) return false;
        repo.deleteById(id);
        return true;
    }
}
