package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetTransactionsByPaymentMethodUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTransactionsByPaymentMethodUseCaseImpl implements GetTransactionsByPaymentMethodUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> findByPaymentMethod(PaymentMethodType paymentMethodType) {
        return paymentRepositoryPort.findByPaymentMethod(paymentMethodType);
    }
}
