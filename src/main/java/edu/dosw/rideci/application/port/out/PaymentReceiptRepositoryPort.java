package edu.dosw.rideci.application.port.out;

import edu.dosw.rideci.domain.model.PaymentReceipt;
import java.util.List;
import java.util.Optional;

public interface PaymentReceiptRepositoryPort {

    PaymentReceipt save(PaymentReceipt receipt);

    Optional<PaymentReceipt> findById(String id);

    Optional<PaymentReceipt> findByReceiptCode(String receiptCode);

    Optional<PaymentReceipt> findByTransactionId(String transactionId);

    List<PaymentReceipt> findAll();
}
