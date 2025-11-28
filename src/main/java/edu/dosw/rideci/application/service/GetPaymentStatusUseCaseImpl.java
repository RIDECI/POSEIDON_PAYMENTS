package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetPaymentStatusUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentStatusUseCaseImpl implements GetPaymentStatusUseCase {

    private final PaymentRepositoryPort repository;

    @Override
    public List<Transaction> findByStatus(TransactionStatus status) {
        return repository.findByStatus(status);
    }
}
