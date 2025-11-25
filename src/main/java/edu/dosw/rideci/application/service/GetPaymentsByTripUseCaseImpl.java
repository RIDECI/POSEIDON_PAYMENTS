package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetPaymentsByTripUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentsByTripUseCaseImpl implements GetPaymentsByTripUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> getByTripId(String tripId) {
        return paymentRepositoryPort.findByBookingId(tripId);
    }
}
