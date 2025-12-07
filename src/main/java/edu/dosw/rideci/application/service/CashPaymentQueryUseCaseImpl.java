package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import edu.dosw.rideci.application.port.in.CashPaymentQueryUseCase;
import edu.dosw.rideci.application.port.out.CashPaymentConfirmationRepositoryPort;
import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.exceptions.RideciBusinessException;

@Service
@RequiredArgsConstructor
public class CashPaymentQueryUseCaseImpl implements CashPaymentQueryUseCase {

    private final CashPaymentConfirmationRepositoryPort cashRepo;

    @Override
    public CashPaymentConfirmation getById(String id) {
        return cashRepo.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Cash confirmation not found: " + id));
    }

    @Override
    public CashPaymentConfirmation getByTransactionId(String transactionId) {
        return cashRepo.findByTransactionId(transactionId)
                .orElseThrow(() -> new RideciBusinessException("No confirmation found for transaction: " + transactionId));
    }

    @Override
    public List<CashPaymentConfirmation> getAll() {
        return cashRepo.findAll();
    }
}
