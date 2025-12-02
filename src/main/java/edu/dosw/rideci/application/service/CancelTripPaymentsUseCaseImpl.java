package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CancelTripPaymentsUseCase;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelTripPaymentsUseCaseImpl implements CancelTripPaymentsUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> cancelTripPayments(String tripId) {
        log.info("Cancelling payments for trip: {}", tripId);
        
        
        List<Transaction> tripPayments = paymentRepositoryPort.findByBookingId(tripId);
        
        if (tripPayments.isEmpty()) {
            log.warn("No payments found for trip: {}", tripId);
            throw new RideciBusinessException("No payments found for trip: " + tripId);
        }

        
        List<Transaction> cancelledPayments = tripPayments.stream()
                .filter(payment -> canBeCancelled(payment.getStatus()))
                .map(payment -> {
                    payment.setStatus(TransactionStatus.CANCELLED);
                    payment.setErrorMessage("Trip was cancelled");
                    return paymentRepositoryPort.save(payment);
                })
                .collect(Collectors.toList());

        log.info("Cancelled {} payments for trip: {}", cancelledPayments.size(), tripId);
        return cancelledPayments;
    }

    private boolean canBeCancelled(TransactionStatus status) {
        return status != TransactionStatus.COMPLETED 
            && status != TransactionStatus.CANCELLED 
            && status != TransactionStatus.FAILED
            && status != TransactionStatus.REFUNDED;
    }
}
