package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.SetDefaultPaymentMethodUseCase;
import edu.dosw.rideci.application.port.out.PaymentMethodRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetDefaultPaymentMethodUseCaseImpl implements SetDefaultPaymentMethodUseCase {

    private final PaymentMethodRepositoryPort repo;

    @Override
    public PaymentMethod setDefault(String id) {

        var method = repo.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Method not found"));

        repo.findDefaultForUser(method.getUserId())
                .ifPresent(defaultMethod -> {
                    defaultMethod.setDefault(false);
                    repo.save(defaultMethod);
                });

        method.setDefault(true);
        return repo.save(method);
    }
}
