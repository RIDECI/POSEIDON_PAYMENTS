package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizePaymentAdapter implements AuthorizePaymentPort {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public Optional<Transaction> authorizePayment(String id, boolean authorized) {
        Transaction tx = paymentRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!authorized) {
            tx.setStatus(TransactionStatus.FAILED);
        } else {
            tx.setStatus(TransactionStatus.AUTHORIZED);
        }
        return Optional.of(paymentRepositoryPort.save(tx));
    }

    @Override
    public boolean isAuthorized(String id) {
        Transaction tx = paymentRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return tx.getStatus() == TransactionStatus.AUTHORIZED;
    }
}
