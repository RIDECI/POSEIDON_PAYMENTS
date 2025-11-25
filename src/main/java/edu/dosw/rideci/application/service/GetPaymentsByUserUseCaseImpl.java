package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetPaymentsByUserUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsByUserUseCaseImpl implements GetPaymentsByUserUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> getByUserId(String userId) {
        return paymentRepositoryPort.findByPassengerId(userId);
    }
}
