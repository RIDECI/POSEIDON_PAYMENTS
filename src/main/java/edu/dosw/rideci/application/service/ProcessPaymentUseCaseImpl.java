package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ProcessPaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.service.PaymentMethodFactory;
import edu.dosw.rideci.domain.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentMethodFactory factory = new PaymentMethodFactory();

    @Override
    public Transaction process(String id) {
        Transaction tx = paymentRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));

        // En un caso real, recuperas el payment method y su tipo; acá demo con NEQUI:
        PaymentStrategy strategy = factory.createStrategy(PaymentMethodType.NEQUI);
        Transaction processed = strategy.processPayment(tx);
        return paymentRepositoryPort.save(processed);
    }
}
