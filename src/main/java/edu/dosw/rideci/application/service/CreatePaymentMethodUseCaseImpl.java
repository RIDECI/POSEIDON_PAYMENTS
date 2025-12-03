package edu.dosw.rideci.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CreatePaymentMethodUseCase;
import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentMethod;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePaymentMethodUseCaseImpl implements CreatePaymentMethodUseCase {

    private final PaymentMethodRepositoryPort repo;

    @Override
    public PaymentMethod create(PaymentMethod method) {

        method.setId("PM-" + UUID.randomUUID());
        method.setActive(true);

        if (repo.findByUserId(method.getUserId()).isEmpty()) {
            method.setDefault(true);
        }

        return repo.save(method);
    }
}

