package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentSuspensionRepositoryPort;
import edu.dosw.rideci.application.service.CreateSuspensionUseCaseImpl;
import edu.dosw.rideci.domain.model.PaymentSuspension;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.SuspensionStatus;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateSuspensionUseCaseImplTest {

    private PaymentSuspensionRepositoryPort repository;
    private PaymentRepositoryPort paymentRepository;
    private CreateSuspensionUseCaseImpl createSuspensionUseCase;

    @BeforeEach
    void setUp() {
        repository = mock(PaymentSuspensionRepositoryPort.class);
        paymentRepository = mock(PaymentRepositoryPort.class);
        createSuspensionUseCase = new CreateSuspensionUseCaseImpl(repository, paymentRepository);
    }

    @Test
    void createSuspension_successful() {
        Transaction tx = new Transaction();
        tx.setId("tx1");
        tx.setStatus(TransactionStatus.PENDING);

        PaymentSuspension input = PaymentSuspension.builder()
                .transactionId("tx1")
                .reason("Test reason")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .adminId("admin1")
                .build();

        when(paymentRepository.findById("tx1")).thenReturn(Optional.of(tx));
        when(repository.findActiveByTransactionId("tx1")).thenReturn(Optional.empty());
        when(repository.save(any(PaymentSuspension.class))).thenAnswer(i -> i.getArgument(0));

        PaymentSuspension saved = createSuspensionUseCase.create(input);

        assertEquals("tx1", saved.getTransactionId());
        assertEquals(SuspensionStatus.ACTIVE, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        verify(repository).save(any(PaymentSuspension.class));
    }

    @Test
    void createSuspension_transactionIdNull_throws() {
        PaymentSuspension input = PaymentSuspension.builder().build();

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            createSuspensionUseCase.create(input);
        });

        assertEquals("transactionId is required", ex.getMessage());
    }

    @Test
    void createSuspension_transactionNotFound_throws() {
        PaymentSuspension input = PaymentSuspension.builder().transactionId("tx2").build();
        when(paymentRepository.findById("tx2")).thenReturn(Optional.empty());

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            createSuspensionUseCase.create(input);
        });

        assertEquals("Transaction not found: tx2", ex.getMessage());
    }

    @Test
    void createSuspension_invalidTransactionStatus_throws() {
        Transaction tx = new Transaction();
        tx.setId("tx3");
        tx.setStatus(TransactionStatus.COMPLETED);

        PaymentSuspension input = PaymentSuspension.builder().transactionId("tx3").build();
        when(paymentRepository.findById("tx3")).thenReturn(Optional.of(tx));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            createSuspensionUseCase.create(input);
        });

        assertEquals("Cannot create suspension for transaction in status: COMPLETED", ex.getMessage());
    }

    @Test
    void createSuspension_existingActiveSuspension_throws() {
        Transaction tx = new Transaction();
        tx.setId("tx4");
        tx.setStatus(TransactionStatus.PENDING);

        PaymentSuspension input = PaymentSuspension.builder().transactionId("tx4").build();
        PaymentSuspension existing = PaymentSuspension.builder().transactionId("tx4").status(SuspensionStatus.ACTIVE).build();

        when(paymentRepository.findById("tx4")).thenReturn(Optional.of(tx));
        when(repository.findActiveByTransactionId("tx4")).thenReturn(Optional.of(existing));

        RideciBusinessException ex = assertThrows(RideciBusinessException.class, () -> {
            createSuspensionUseCase.create(input);
        });

        assertEquals("An active suspension already exists for this transaction", ex.getMessage());
    }
}
