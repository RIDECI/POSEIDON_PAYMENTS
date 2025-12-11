package edu.dosw.rideci.application.port.in;

public interface GenerateReceiptPdfUseCase {
    byte[] generatePdf(String receiptId);
}
