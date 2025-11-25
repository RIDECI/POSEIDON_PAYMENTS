package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetPaymentsByDateUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsByDateUseCaseImpl implements GetPaymentsByDateUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> getByDate(LocalDate date) {
        return paymentRepositoryPort.findByCreatedAtDate(date);
    }
}
