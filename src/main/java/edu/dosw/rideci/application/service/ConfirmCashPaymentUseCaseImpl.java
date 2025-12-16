package edu.dosw.rideci.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.ConfirmCashPaymentUseCase;
import edu.dosw.rideci.application.port.out.CashPaymentConfirmationRepositoryPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfirmCashPaymentUseCaseImpl implements ConfirmCashPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final CashPaymentConfirmationRepositoryPort cashRepo;

    @Override
    public CashPaymentConfirmation confirm(String transactionId, String driverId, String observations) {

        var tx = paymentRepositoryPort.findById(transactionId)
                .orElseThrow(() -> new RideciBusinessException("Transaction not found"));

    
        if (tx.getPaymentMethod() != PaymentMethodType.CASH) {
            throw new RideciBusinessException("Only CASH payments can be confirmed");
        }

    
        if (tx.getStatus() != TransactionStatus.PENDING_CASH) {
            throw new RideciBusinessException("Cash payment is not pending confirmation");
        }

     
        if (cashRepo.findByTransactionId(transactionId).isPresent()) {
            throw new RideciBusinessException("Cash payment already confirmed");
        }

     
        CashPaymentConfirmation confirmation = CashPaymentConfirmation.builder()
                .id("CASH-" + UUID.randomUUID())
                .transactionId(tx.getId())
                .bookingId(tx.getBookingId())
                .passengerId(tx.getPassengerId())
                .driverId(driverId)
                .amount(tx.getAmount())
                .confirmed(true)
                .confirmedAt(LocalDateTime.now())
                .observations(observations)
                .build();


        cashRepo.save(confirmation);

 
        tx.setStatus(TransactionStatus.COMPLETED);
        paymentRepositoryPort.save(tx);

        return confirmation;
    }
}
