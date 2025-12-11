package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.PaymentReceipt;
import java.util.List;

public interface GetPaymentReceiptUseCase {

    PaymentReceipt getById(String id);

    PaymentReceipt getByReceiptCode(String receiptCode);

    PaymentReceipt getByTransactionId(String transactionId);

    List<PaymentReceipt> getAll();
}
