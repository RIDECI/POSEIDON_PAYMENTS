package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.RefundPaymentUseCaseImpl;
import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefundPaymentUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private RefundRepositoryPort refundRepositoryPort;
    private CreateAuditLogUseCase createAuditLogUseCase;
    private RefundPaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        refundRepositoryPort = mock(RefundRepositoryPort.class);
        createAuditLogUseCase = mock(CreateAuditLogUseCase.class);
        useCase = new RefundPaymentUseCaseImpl(paymentRepositoryPort, refundRepositoryPort, createAuditLogUseCase);
    }

    @Test
    void refundPayment_transactionNotFound_throwsException() {
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.refundPayment("tx1", 100.0, "Reason"));

        assertEquals("Transaction not found: tx1", ex.getMessage());
    }

    @Test
    void refundPayment_transactionNotCompleted_throwsException() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.PROCESSING).amount(200.0).build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.refundPayment("tx1", 100.0, "Reason"));

        assertTrue(ex.getMessage().contains("Only COMPLETED transactions can be refunded"));
    }

    @Test
    void refundPayment_existingRefund_throwsException() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.COMPLETED).amount(200.0).build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(refundRepositoryPort.findByTransactionId("tx1")).thenReturn(Refund.builder().build());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.refundPayment("tx1", 100.0, "Reason"));

        assertEquals("This transaction already has a refund request", ex.getMessage());
    }

    @Test
    void refundPayment_invalidAmount_throwsException() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.COMPLETED).amount(200.0).build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(refundRepositoryPort.findByTransactionId("tx1")).thenReturn(null);

        // Amount <= 0
        RideciBusinessException ex1 = assertThrows(RideciBusinessException.class,
                () -> useCase.refundPayment("tx1", 0.0, "Reason"));
        assertEquals("Refund amount must be greater than zero", ex1.getMessage());

        // Amount > transaction
        RideciBusinessException ex2 = assertThrows(RideciBusinessException.class,
                () -> useCase.refundPayment("tx1", 500.0, "Reason"));
        assertTrue(ex2.getMessage().contains("Refund amount (500.0) exceeds transaction amount (200.0)"));
    }

    @Test
    void refundPayment_successfulRefund_createsRefundAndAuditLog() {
        Transaction tx = Transaction.builder()
                .id("tx1").status(TransactionStatus.COMPLETED).amount(200.0)
                .bookingId("bk1").passengerId("user1")
                .build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(refundRepositoryPort.findByTransactionId("tx1")).thenReturn(null);

        when(refundRepositoryPort.save(any(Refund.class))).thenAnswer(i -> i.getArguments()[0]);
        when(createAuditLogUseCase.createAuditLog(any(AuditLog.class))).thenReturn(AuditLog.builder().build());

        Refund refund = useCase.refundPayment("tx1", 100.0, "Customer request");

        assertEquals(RefundStatus.REQUESTED, refund.getStatus());
        assertEquals(100.0, refund.getRefundedAmount());
        assertEquals("tx1", refund.getTransactionId());

        verify(refundRepositoryPort).save(any(Refund.class));
        verify(createAuditLogUseCase).createAuditLog(any(AuditLog.class));
    }
}
