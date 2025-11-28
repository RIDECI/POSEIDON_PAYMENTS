package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetPaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetPaymentUseCaseImpl implements GetPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Optional<Transaction> getById(String id) {
        return paymentRepositoryPort.findById(id);
    }
    @Override
    public List<Transaction> findAll(){
        return paymentRepositoryPort.findAll();
    }
}
