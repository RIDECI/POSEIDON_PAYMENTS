package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.ProcessWebhookUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessWebhookUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private ProcessWebhookUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        useCase = new ProcessWebhookUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void processWebhook_missingTransactionId_throwsException() {
        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.processWebhook("PayU", "", "ref1", "COMPLETED", 100.0, "CREDIT_CARD_PAYU", null));

        assertEquals("Transaction ID is required", ex.getMessage());
    }

    @Test
    void processWebhook_transactionNotFound_throwsException() {
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class,
                () -> useCase.processWebhook("PayU", "tx1", "ref1", "COMPLETED", 100.0, "CREDIT_CARD_PAYU", null));

        assertEquals("Payment not found: tx1", ex.getMessage());
    }

    @Test
    void processWebhook_statusMappingAndMetadata_successful() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.PENDING).build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.processWebhook(
                "PayU",
                "tx1",
                "ref1",
                "COMPLETED",
                100.0,
                "CREDIT_CARD_PAYU",
                "extra-metadata"
        );

        assertEquals(TransactionStatus.COMPLETED, result.getStatus());
        assertEquals("extra-metadata", result.getExtra());
        verify(paymentRepositoryPort).save(any(Transaction.class));
    }

    @Test
    void processWebhook_unknownStatus_logsWarningButDoesNotChangeStatus() {
        Transaction tx = Transaction.builder().id("tx1").status(TransactionStatus.PENDING).build();
        when(paymentRepositoryPort.findById("tx1")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = useCase.processWebhook(
                "PayU",
                "tx1",
                "ref1",
                "UNKNOWN_STATUS",
                100.0,
                "CREDIT_CARD_PAYU",
                null
        );

        assertEquals(TransactionStatus.PENDING, result.getStatus());
        verify(paymentRepositoryPort).save(any(Transaction.class));
    }
}
