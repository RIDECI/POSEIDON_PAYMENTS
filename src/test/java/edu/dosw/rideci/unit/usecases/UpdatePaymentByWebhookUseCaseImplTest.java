package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.UpdatePaymentByWebhookUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdatePaymentByWebhookUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private UpdatePaymentByWebhookUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        useCase = new UpdatePaymentByWebhookUseCaseImpl(repository);
    }

    @Test
    void update_paymentNotFound_throwsException() {
        when(repository.findById("TX-1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.updateByWebhook("PayU", "TX-1", "AUTHORIZED", null, null, null));

        assertEquals("Payment not found: TX-1", ex.getMessage());
    }

    @Test
    void update_paymentCompleted_throwsException() {
        Transaction tx = Transaction.builder()
                .id("TX-2")
                .status(TransactionStatus.COMPLETED)
                .build();
        when(repository.findById("TX-2")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.updateByWebhook("PayU", "TX-2", "FAILED", null, null, null));

        assertEquals("Cannot update completed payment", ex.getMessage());
    }

    @Test
    void update_paymentRefunded_throwsException() {
        Transaction tx = Transaction.builder()
                .id("TX-3")
                .status(TransactionStatus.REFUNDED)
                .build();
        when(repository.findById("TX-3")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.updateByWebhook("PayU", "TX-3", "FAILED", null, null, null));

        assertEquals("Cannot update refunded payment", ex.getMessage());
    }

    @Test
    void update_successfulUpdate_changesFields() {
        Transaction tx = Transaction.builder()
                .id("TX-4")
                .status(TransactionStatus.PENDING)
                .build();
        when(repository.findById("TX-4")).thenReturn(Optional.of(tx));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction updated = useCase.updateByWebhook(
                "PayU",
                "TX-4",
                "AUTHORIZED",
                "RCPT123",
                "Some error",
                "meta=xyz"
        );

        assertEquals(TransactionStatus.AUTHORIZED, updated.getStatus());
        assertEquals("RCPT123", updated.getReceiptCode());
        assertEquals("Some error", updated.getErrorMessage());
        assertEquals("meta=xyz", updated.getExtra());
    }
}
