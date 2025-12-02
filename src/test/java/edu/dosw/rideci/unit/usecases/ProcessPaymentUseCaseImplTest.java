package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.ProcessPaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.domain.service.PaymentMethodFactory;
import edu.dosw.rideci.domain.service.PaymentStrategy;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessPaymentUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private PaymentMethodFactory factory;
    private AuthorizePaymentPort authorizationPort;
    private PaymentSuspensionRepositoryPort suspensionRepository;
    private ProcessPaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        factory = mock(PaymentMethodFactory.class);
        authorizationPort = mock(AuthorizePaymentPort.class);
        suspensionRepository = mock(PaymentSuspensionRepositoryPort.class);
        useCase = new ProcessPaymentUseCaseImpl(repository, factory, authorizationPort, suspensionRepository);
    }

    @Test
    void process_notAuthorized_throwsException() {
        when(authorizationPort.isAuthorized("tx1")).thenReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> useCase.process("tx1"));

        assertEquals("Payment must be authorized before processing", ex.getMessage());
    }

    @Test
    void process_transactionNotFound_throwsException() {
        when(authorizationPort.isAuthorized("tx1")).thenReturn(true);
        when(repository.findById("tx1")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> useCase.process("tx1"));

        assertEquals("Payment not found", ex.getMessage());
    }

    @Test
    void process_activeSuspension_throwsException() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.AUTHORIZED).build();
        PaymentSuspension suspension = mock(PaymentSuspension.class);

        when(authorizationPort.isAuthorized("tx1")).thenReturn(true);
        when(repository.findById("tx1")).thenReturn(Optional.of(tx));
        when(suspensionRepository.findActiveByTransactionId("tx1")).thenReturn(Optional.of(suspension));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.process("tx1"));

        assertTrue(ex.getMessage().contains("Payment suspended"));
    }

    @Test
    void process_notAuthorizedStatus_throwsException() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.PENDING).build();

        when(authorizationPort.isAuthorized("tx1")).thenReturn(true);
        when(repository.findById("tx1")).thenReturn(Optional.of(tx));
        when(suspensionRepository.findActiveByTransactionId("tx1")).thenReturn(Optional.empty());

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> useCase.process("tx1"));

        assertEquals("Solo se pueden procesar pagos AUTHORIZED", ex.getMessage());
    }

    @Test
    void process_successful() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.AUTHORIZED).build();
        Transaction processedTx = Transaction.builder().id("tx1").status(TransactionStatus.COMPLETED).build();
        PaymentStrategy strategy = mock(PaymentStrategy.class);

        when(authorizationPort.isAuthorized("tx1")).thenReturn(true);
        when(repository.findById("tx1")).thenReturn(Optional.of(tx));
        when(suspensionRepository.findActiveByTransactionId("tx1")).thenReturn(Optional.empty());
        when(factory.createStrategy(tx.getPaymentMethod())).thenReturn(strategy);
        when(strategy.processPayment(tx)).thenReturn(processedTx);
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.process("tx1");

        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        verify(repository, times(2)).save(any(Transaction.class)); // PROCESSING + resultado final
    }
}
