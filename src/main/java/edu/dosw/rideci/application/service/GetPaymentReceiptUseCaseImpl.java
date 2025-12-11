package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetPaymentReceiptUseCase;
import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPaymentReceiptUseCaseImpl implements GetPaymentReceiptUseCase {

    private final PaymentReceiptRepositoryPort repo;

    @Override
    public PaymentReceipt getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RideciBusinessException("Receipt not found: " + id));
    }

    @Override
    public PaymentReceipt getByReceiptCode(String receiptCode) {
        return repo.findByReceiptCode(receiptCode)
                .orElseThrow(() -> new RideciBusinessException("Receipt code not found: " + receiptCode));
    }

    @Override
    public PaymentReceipt getByTransactionId(String transactionId) {
        return repo.findByTransactionId(transactionId)
                .orElseThrow(() -> new RideciBusinessException("Receipt for transaction not found: " + transactionId));
    }

    @Override
    public List<PaymentReceipt> getAll() {
        return repo.findAll();
    }
}
