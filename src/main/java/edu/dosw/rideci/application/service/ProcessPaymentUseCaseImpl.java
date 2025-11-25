package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.ProcessPaymentUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.domain.service.PaymentMethodFactory;
import edu.dosw.rideci.domain.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final PaymentRepositoryPort repository;
    private final PaymentMethodFactory factory;
    private final AuthorizePaymentPort authorizationPort; // ðŸ”¥ FALTABA ESTO

    @Override
    public Transaction process(String id) {

        // 1. ValidaciÃ³n externa (port OUT)
        if (!authorizationPort.isAuthorized(id)) {
            throw new IllegalStateException("Payment must be authorized before processing");
        }

        // 2. Buscar transacciÃ³n
        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // 3. Validar estado AUTHORIZED
        if (tx.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new IllegalStateException("Solo se pueden procesar pagos AUTHORIZED");
        }

        // 4. Cambiar a PROCESSING
        tx.setStatus(TransactionStatus.PROCESSING);
        repository.save(tx);

        // 5. Ejecutar estrategia (PayU, Nequi, BreB, Cashâ€¦)
        PaymentStrategy strategy = factory.createStrategy(tx.getPaymentMethod());
        tx = strategy.processPayment(tx);

        // 6. Guardar resultado final (COMPLETED / FAILED)
        return repository.save(tx);
    }
}
