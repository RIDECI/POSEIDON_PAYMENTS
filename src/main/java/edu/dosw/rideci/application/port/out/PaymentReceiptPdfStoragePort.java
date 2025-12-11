package edu.dosw.rideci.application.port.out;

public interface PaymentReceiptPdfStoragePort {

    void save(String receiptId, byte[] pdfBytes);

}
