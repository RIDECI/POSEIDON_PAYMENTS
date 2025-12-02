package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.CreatePaymentUseCaseImpl;
import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePaymentUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private CreateAuditLogUseCase createAuditLogUseCase;
    private CreatePaymentUseCaseImpl createPaymentUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        createAuditLogUseCase = mock(CreateAuditLogUseCase.class);
        createPaymentUseCase = new CreatePaymentUseCaseImpl(paymentRepositoryPort, createAuditLogUseCase);
    }

    @Test
    void createPayment_successful() {
        Transaction tx = Transaction.builder()
                .id("tx1")
                .bookingId("bk1")
                .passengerId("usr1")
                .amount(100.0)
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .build();

        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        when(createAuditLogUseCase.createAuditLog(any(AuditLog.class))).thenAnswer(i -> i.getArgument(0));

        Transaction saved = createPaymentUseCase.create(tx);

        assertEquals(TransactionStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertEquals(100.0, saved.getAmount());

        verify(paymentRepositoryPort).save(tx);

        verify(createAuditLogUseCase).createAuditLog(any(AuditLog.class));
    }

    @Test
    void createPayment_auditLogFails_transactionStillSaved() {
        Transaction tx = Transaction.builder()
                .id("tx2")
                .bookingId("bk2")
                .passengerId("usr2")
                .amount(50.0)
                .paymentMethod(PaymentMethodType.NEQUI)
                .build();

        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        doThrow(new RuntimeException("Audit log failed")).when(createAuditLogUseCase).createAuditLog(any(AuditLog.class));

        Transaction saved = createPaymentUseCase.create(tx);

        assertEquals(TransactionStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        verify(paymentRepositoryPort).save(tx);

        verify(createAuditLogUseCase).createAuditLog(any(AuditLog.class));
    }
}
