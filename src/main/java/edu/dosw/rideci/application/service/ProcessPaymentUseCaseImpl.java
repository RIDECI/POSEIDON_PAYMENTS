package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ProcessPaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.domain.service.PaymentMethodFactory;
import edu.dosw.rideci.domain.service.PaymentStrategy;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final PaymentRepositoryPort repository;
    private final PaymentMethodFactory factory;
    private final AuthorizePaymentPort authorizationPort;
    private final PaymentSuspensionRepositoryPort paymentSuspensionRepositoryPort;

    @Override
    public Transaction process(String id) {

       
        if (!authorizationPort.isAuthorized(id)) {
            throw new RideciBusinessException("Payment must be authorized before processing");
        }

       
        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Payment not found"));

        
        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new RideciBusinessException("Only AUTHORIZED payments can be processed");
        }

        
        if (paymentSuspensionRepositoryPort.findActiveByTransactionId(tx.getId()).isPresent()) {
            throw new RideciBusinessException("Payment suspended. Cannot process until suspension is revoked or expired.");
        }

        
        tx.setStatus(TransactionStatus.PROCESSING);
        repository.save(tx);

    
        PaymentStrategy strategy = factory.createStrategy(tx.getPaymentMethod());
        tx = strategy.processPayment(tx);

        return repository.save(tx);
    }
}
