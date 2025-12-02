package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.CancelPaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelPaymentUseCaseImplTest {

    @Mock
    private PaymentRepositoryPort paymentRepositoryPort;

    @Mock
    private CreateAuditLogUseCase createAuditLogUseCase;

    @InjectMocks
    private CancelPaymentUseCaseImpl cancelPaymentUseCase;

    private Transaction transaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        transaction = Transaction.builder()
                .id("TX-101")
                .status(TransactionStatus.PROCESSING)
                .bookingId("BK-10")
                .passengerId("USR-001")
                .build();
    }

    @Test
    void shouldCancelPaymentSuccessfully() {
        when(paymentRepositoryPort.findById("TX-101"))
                .thenReturn(Optional.of(transaction));
        when(paymentRepositoryPort.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Transaction result = cancelPaymentUseCase.cancel("TX-101");

        assertNotNull(result);
        assertEquals(TransactionStatus.CANCELLED, result.getStatus());

        verify(paymentRepositoryPort).findById("TX-101");
        verify(paymentRepositoryPort).save(transaction);
        verify(createAuditLogUseCase).createAuditLog(any());
    }

    @Test
    void shouldThrowWhenPaymentNotFound() {
        when(paymentRepositoryPort.findById("TX-404"))
                .thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> cancelPaymentUseCase.cancel("TX-404"));

        verify(paymentRepositoryPort).findById("TX-404");
        verify(paymentRepositoryPort, never()).save(any());
    }

    @Test
    void shouldNotCancelCompletedPayment() {
        transaction.setStatus(TransactionStatus.COMPLETED);
        when(paymentRepositoryPort.findById("TX-101"))
                .thenReturn(Optional.of(transaction));

        assertThrows(RideciBusinessException.class,
                () -> cancelPaymentUseCase.cancel("TX-101"));

        verify(paymentRepositoryPort, never()).save(any());
    }

    @Test
    void shouldNotCancelAlreadyCancelledPayment() {
        transaction.setStatus(TransactionStatus.CANCELLED);
        when(paymentRepositoryPort.findById("TX-101"))
                .thenReturn(Optional.of(transaction));

        assertThrows(RideciBusinessException.class,
                () -> cancelPaymentUseCase.cancel("TX-101"));

        verify(paymentRepositoryPort, never()).save(any());
    }

    @Test
    void shouldNotCancelRefundedPayment() {
        transaction.setStatus(TransactionStatus.REFUNDED);
        when(paymentRepositoryPort.findById("TX-101"))
                .thenReturn(Optional.of(transaction));

        assertThrows(RideciBusinessException.class,
                () -> cancelPaymentUseCase.cancel("TX-101"));

        verify(paymentRepositoryPort, never()).save(any());
    }

    @Test
    void shouldStillReturnCancelledPaymentWhenAuditLogFails() {
        when(paymentRepositoryPort.findById("TX-101"))
                .thenReturn(Optional.of(transaction));
        when(paymentRepositoryPort.save(any(Transaction.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        doThrow(new RuntimeException("Audit failed"))
                .when(createAuditLogUseCase)
                .createAuditLog(any());

        Transaction result = cancelPaymentUseCase.cancel("TX-101");

        assertEquals(TransactionStatus.CANCELLED, result.getStatus());
        verify(paymentRepositoryPort).save(transaction);
    }
}
