package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetPaymentMethodsUseCase;
import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentMethod;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetPaymentMethodsUseCaseImpl implements GetPaymentMethodsUseCase {

    private final PaymentMethodRepositoryPort repo;

    @Override
    public List<PaymentMethod> getByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public PaymentMethod getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }

    @Override
    public List<PaymentMethod> findAll() {
        return repo.findAll();
    }

}
