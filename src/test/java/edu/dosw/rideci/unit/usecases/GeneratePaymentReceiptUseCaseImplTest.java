package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GeneratePaymentReceiptUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeneratePaymentReceiptUseCaseImplTest {

    private PaymentRepositoryPort paymentRepo;
    private PaymentReceiptRepositoryPort receiptRepo;
    private GeneratePaymentReceiptUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        paymentRepo = mock(PaymentRepositoryPort.class);
        receiptRepo = mock(PaymentReceiptRepositoryPort.class);
        useCase = new GeneratePaymentReceiptUseCaseImpl(paymentRepo, receiptRepo);
    }

    @Test
    void shouldGenerateReceiptSuccessfully() {
        String txId = "TX-1";
        String driverId = "DRV-9";

        var transaction = Transaction.builder()
                .id(txId)
                .bookingId("BKG-88")
                .passengerId("PAS-44")
                .paymentMethod(PaymentMethodType.CASH)
                .amount(50.0)
                .status(TransactionStatus.COMPLETED)
                .build();

        when(paymentRepo.findById(txId)).thenReturn(Optional.of(transaction));
        when(receiptRepo.findByTransactionId(txId)).thenReturn(Optional.empty());
        when(receiptRepo.save(any())).thenAnswer(i -> i.getArguments()[0]);

        
        PaymentReceipt receipt = useCase.generate(txId, driverId);

        
        assertNotNull(receipt);
        assertEquals(txId, receipt.getTransactionId());
        assertEquals(driverId, receipt.getDriverId());
        assertEquals(50.0, receipt.getAmount());

        verify(receiptRepo).save(any());
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        when(paymentRepo.findById("X")).thenReturn(Optional.empty());

        assertThrows(RideciBusinessException.class,
                () -> useCase.generate("X", "DRV"));
    }

    @Test
    void shouldThrowWhenTransactionNotCompleted() {
        var tx = Transaction.builder()
                .id("T1")
                .status(TransactionStatus.PENDING_CASH)
                .build();

        when(paymentRepo.findById("T1")).thenReturn(Optional.of(tx));

        assertThrows(RideciBusinessException.class,
                () -> useCase.generate("T1", "DRV"));
    }

    @Test
    void shouldThrowWhenReceiptAlreadyExists() {
        var tx = Transaction.builder()
                .id("T1")
                .status(TransactionStatus.COMPLETED)
                .build();

        when(paymentRepo.findById("T1")).thenReturn(Optional.of(tx));
        when(receiptRepo.findByTransactionId("T1"))
                .thenReturn(Optional.of(PaymentReceipt.builder().id("REC").build()));

        assertThrows(RideciBusinessException.class,
                () -> useCase.generate("T1", "DRV"));
    }
}
