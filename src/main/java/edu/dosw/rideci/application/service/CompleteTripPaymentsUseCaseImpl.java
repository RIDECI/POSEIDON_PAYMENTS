package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CompleteTripPaymentsUseCase;
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
public class CompleteTripPaymentsUseCaseImpl implements CompleteTripPaymentsUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    public List<Transaction> completeTripPayments(String tripId) {
        log.info("Completing payments for trip: {}", tripId);
        
        // Buscar todos los pagos del viaje
        List<Transaction> tripPayments = paymentRepositoryPort.findByBookingId(tripId);
        
        if (tripPayments.isEmpty()) {
            log.warn("No payments found for trip: {}", tripId);
            throw new RideciBusinessException("No payments found for trip: " + tripId);
        }

        // Completar solo los pagos que estén en estado APPROVED
        List<Transaction> completedPayments = tripPayments.stream()
                .filter(payment -> payment.getStatus() == TransactionStatus.APPROVED)
                .map(payment -> {
                    payment.setStatus(TransactionStatus.COMPLETED);
                    return paymentRepositoryPort.save(payment);
                })
                .collect(Collectors.toList());

        log.info("Completed {} payments for trip: {}", completedPayments.size(), tripId);
        
        // Verificar si hay pagos pendientes
        long pendingPayments = tripPayments.stream()
                .filter(p -> p.getStatus() != TransactionStatus.COMPLETED 
                          && p.getStatus() != TransactionStatus.CANCELLED
                          && p.getStatus() != TransactionStatus.FAILED)
                .count();
                
        if (pendingPayments > 0) {
            log.warn("Trip {} completed but has {} pending payments", tripId, pendingPayments);
        }

        return completedPayments;
    }
}
