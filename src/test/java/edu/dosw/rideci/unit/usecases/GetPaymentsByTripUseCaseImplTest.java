package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.GetPaymentsByTripUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetPaymentsByTripUseCaseImplTest {

    private PaymentRepositoryPort paymentRepositoryPort;
    private GetPaymentsByTripUseCaseImpl getPaymentsByTripUseCase;

    @BeforeEach
    void setUp() {
        paymentRepositoryPort = mock(PaymentRepositoryPort.class);
        getPaymentsByTripUseCase = new GetPaymentsByTripUseCaseImpl(paymentRepositoryPort);
    }

    @Test
    void getByTripId_returnsList() {
        String tripId = "trip123";
        Transaction tx1 = Transaction.builder().id("tx1").build();
        Transaction tx2 = Transaction.builder().id("tx2").build();
        List<Transaction> transactions = Arrays.asList(tx1, tx2);

        when(paymentRepositoryPort.findByBookingId(tripId)).thenReturn(transactions);

        List<Transaction> result = getPaymentsByTripUseCase.getByTripId(tripId);

        assertEquals(2, result.size());
        assertTrue(result.contains(tx1));
        assertTrue(result.contains(tx2));
        verify(paymentRepositoryPort).findByBookingId(tripId);
    }

    @Test
    void getByTripId_emptyList() {
        String tripId = "trip456";

        when(paymentRepositoryPort.findByBookingId(tripId)).thenReturn(List.of());

        List<Transaction> result = getPaymentsByTripUseCase.getByTripId(tripId);

        assertTrue(result.isEmpty());
        verify(paymentRepositoryPort).findByBookingId(tripId);
    }
}
