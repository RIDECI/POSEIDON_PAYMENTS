package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.CompletePaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompletePaymentUseCaseImplTest {

    private PaymentRepositoryPort repository;
    private CompletePaymentUseCaseImpl completePaymentUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentRepositoryPort.class);
        completePaymentUseCase = new CompletePaymentUseCaseImpl(repository);
    }

    @Test
    void complete_successful_digitalPayment() {
        Transaction tx = new Transaction();
        tx.setId("tx1");
        tx.setStatus(TransactionStatus.APPROVED);
        tx.setAmount(100.0);
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setReceiptCode("12345");

        when(repository.findById("tx1")).thenReturn(Optional.of(tx));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction completed = completePaymentUseCase.complete("tx1");

        assertEquals(TransactionStatus.COMPLETED, completed.getStatus());
        assertTrue(completed.getExtra().contains("COMPLETED:true"));
        verify(repository).save(tx);
    }

    @Test
    void complete_successful_cashPayment() {
        Transaction tx = new Transaction();
        tx.setId("tx2");
        tx.setStatus(TransactionStatus.APPROVED);
        tx.setAmount(50.0);
        tx.setPaymentMethod(PaymentMethodType.CASH);
        tx.setReceiptCode(null);

        when(repository.findById("tx2")).thenReturn(Optional.of(tx));
        when(repository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction completed = completePaymentUseCase.complete("tx2");

        assertEquals(TransactionStatus.COMPLETED, completed.getStatus());
        assertTrue(completed.getExtra().contains("COMPLETED:true"));
        verify(repository).save(tx);
    }

    @Test
    void complete_paymentAlreadyCompleted() {
        Transaction tx = new Transaction();
        tx.setId("tx3");
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setAmount(100.0);
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setReceiptCode("12345");

        when(repository.findById("tx3")).thenReturn(Optional.of(tx));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            completePaymentUseCase.complete("tx3");
        });

        assertEquals("El pago ya fue completado previamente", ex.getMessage());
    }

    @Test
    void complete_notApproved() {
        Transaction tx = new Transaction();
        tx.setId("tx4");
        tx.setStatus(TransactionStatus.PENDING);
        tx.setAmount(100.0);
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setReceiptCode("12345");

        when(repository.findById("tx4")).thenReturn(Optional.of(tx));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            completePaymentUseCase.complete("tx4");
        });

        assertEquals("Solo se pueden completar pagos en estado APPROVED", ex.getMessage());
    }

    @Test
    void complete_invalidAmount() {
        Transaction tx = new Transaction();
        tx.setId("tx5");
        tx.setStatus(TransactionStatus.APPROVED);
        tx.setAmount(0.0);
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setReceiptCode("12345");

        when(repository.findById("tx5")).thenReturn(Optional.of(tx));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            completePaymentUseCase.complete("tx5");
        });

        assertEquals("Monto inválido para completar el pago", ex.getMessage());
    }

    @Test
    void complete_missingReceiptCode_digital() {
        Transaction tx = new Transaction();
        tx.setId("tx6");
        tx.setStatus(TransactionStatus.APPROVED);
        tx.setAmount(100.0);
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setReceiptCode(" "); 
        when(repository.findById("tx6")).thenReturn(Optional.of(tx));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            completePaymentUseCase.complete("tx6");
        });

        assertEquals("receiptCode requerido para pagos digitales", ex.getMessage());
    }

    @Test
    void complete_paymentNotFound() {
        when(repository.findById("tx7")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            completePaymentUseCase.complete("tx7");
        });

        assertEquals("Payment not found", ex.getMessage());
    }
}
