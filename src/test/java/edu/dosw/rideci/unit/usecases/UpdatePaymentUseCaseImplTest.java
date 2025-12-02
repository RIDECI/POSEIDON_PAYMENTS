package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.UpdatePaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.controller.dto.Request.UpdatePaymentRequest;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdatePaymentUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private UpdatePaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        useCase = new UpdatePaymentUseCaseImpl(repository);
    }

    @Test
    void update_paymentNotFound_throwsException() {
        when(repository.findById("TX-1")).thenReturn(Optional.empty());

        Supplier<Transaction> action = () -> useCase.updatePartial("TX-1", new UpdatePaymentRequest());

        RuntimeException ex = assertThrows(RuntimeException.class, action::get);

        assertEquals("Payment not found", ex.getMessage());
    }

    @Test
    void update_paymentNotPending_throwsException() {
        Transaction tx = Transaction.builder()
                .id("TX-2")
                .status(TransactionStatus.COMPLETED)
                .build();
        when(repository.findById("TX-2")).thenReturn(Optional.of(tx));

        Supplier<Transaction> action = () -> useCase.updatePartial("TX-2", new UpdatePaymentRequest());

        IllegalStateException ex = assertThrows(IllegalStateException.class, action::get);

        assertEquals("Solo se pueden modificar pagos con estado PENDING", ex.getMessage());
    }

    @Test
    void update_receiptCodeForCash_throwsException() {
        Transaction tx = Transaction.builder()
                .id("TX-3")
                .status(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethodType.CASH)
                .build();
        when(repository.findById("TX-3")).thenReturn(Optional.of(tx));

        UpdatePaymentRequest req = new UpdatePaymentRequest();
        req.setReceiptCode("RCPT-123");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.updatePartial("TX-3", req));

        assertEquals("receiptCode no permitido para pagos CASH", ex.getMessage());
    }

    @Test
    void update_successfulUpdate_changesFields() {
        Transaction tx = Transaction.builder()
                .id("TX-4")
                .status(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .amount(100.0)
                .build();
        when(repository.findById("TX-4")).thenReturn(Optional.of(tx));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        UpdatePaymentRequest req = new UpdatePaymentRequest();
        req.setAmount(150.0);
        req.setPaymentMethod(PaymentMethodType.NEQUI);
        req.setExtra("new extra");
        req.setReceiptCode("RCPT-456");

        Transaction updated = useCase.updatePartial("TX-4", req);

        assertEquals(150.0, updated.getAmount());
        assertEquals(PaymentMethodType.NEQUI, updated.getPaymentMethod());
        assertEquals("new extra", updated.getExtra());
        assertEquals("RCPT-456", updated.getReceiptCode());
    }

    @Test
    void update_nullFields_doesNotChange() {
        Transaction tx = Transaction.builder()
                .id("TX-5")
                .status(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .amount(200.0)
                .extra("old")
                .receiptCode("OLD-123")
                .build();
        when(repository.findById("TX-5")).thenReturn(Optional.of(tx));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        UpdatePaymentRequest req = new UpdatePaymentRequest();

        Transaction updated = useCase.updatePartial("TX-5", req);

        assertEquals(200.0, updated.getAmount());
        assertEquals(PaymentMethodType.CREDIT_CARD_PAYU, updated.getPaymentMethod());
        assertEquals("old", updated.getExtra());
        assertEquals("OLD-123", updated.getReceiptCode());
    }
}
