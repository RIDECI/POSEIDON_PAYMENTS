package edu.dosw.rideci.application.service;


import edu.dosw.rideci.application.port.in.GetSuspensionUseCase;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetSuspensionUseCaseImpl implements GetSuspensionUseCase {

    private final PaymentSuspensionRepositoryPort repository;

    @Override
    public Optional<PaymentSuspension> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<PaymentSuspension> getAll() {
        return repository.findAll();
    }
}
