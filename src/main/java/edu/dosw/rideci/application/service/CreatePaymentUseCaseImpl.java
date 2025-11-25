package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.CreatePaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Transaction create(Transaction tx) {
        tx.setStatus(TransactionStatus.PENDING);
        tx.setCreatedAt(LocalDateTime.now());
        return paymentRepositoryPort.save(tx);
    }
    
}
