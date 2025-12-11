package edu.dosw.rideci.application.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;

import edu.dosw.rideci.application.port.in.GenerateReceiptPdfUseCase;
import edu.dosw.rideci.application.port.out.PaymentReceiptRepositoryPort;
import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.exceptions.RideciBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class GenerateReceiptPdfUseCaseImpl implements GenerateReceiptPdfUseCase {

    private final PaymentReceiptRepositoryPort receiptRepo;

    @Override
    public byte[] generatePdf(String receiptId) {

        PaymentReceipt receipt = receiptRepo.findById(receiptId)
                .orElseThrow(() -> new RideciBusinessException("Receipt not found: " + receiptId));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4, 40, 40, 40, 40);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Header
            Paragraph title = new Paragraph("PAYMENT RECEIPT",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            // Table
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);
            table.setWidths(new float[]{2, 4});

            addRow(table, "Receipt ID:", receipt.getId());
            addRow(table, "Receipt Code:", receipt.getReceiptCode());
            addRow(table, "Transaction ID:", receipt.getTransactionId());
            addRow(table, "Passenger ID:", receipt.getPassengerId());
            addRow(table, "Driver ID:", receipt.getDriverId());
            addRow(table, "Booking ID:", receipt.getBookingId());
            addRow(table, "Payment Method:", receipt.getPaymentMethod());
            addRow(table, "Issued At:", String.valueOf(receipt.getIssuedAt()));

            document.add(table);

            // Amount box
            Paragraph amountTitle = new Paragraph("TOTAL PAID:",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            document.add(amountTitle);

            Paragraph amountValue = new Paragraph("$ " + receipt.getAmount(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28));
            amountValue.setSpacingBefore(5);
            amountValue.setAlignment(Element.ALIGN_LEFT);
            document.add(amountValue);

            document.add(new Paragraph("\n\n"));

            // Footer
            Paragraph footer = new Paragraph(
                    "This receipt is a legal proof of payment for the associated transaction.\n" +
                            "Thank you for using our service.",
                    FontFactory.getFont(FontFactory.HELVETICA, 10)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            throw new RideciBusinessException("Error generating PDF: " + e.getMessage());
        }

        return baos.toByteArray();
    }

    private void addRow(PdfPTable table, String label, String value) {
        Phrase labelPhrase = new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        Phrase valuePhrase = new Phrase(value != null ? value : "-", FontFactory.getFont(FontFactory.HELVETICA, 12));

        table.addCell(labelPhrase);
        table.addCell(valuePhrase);
    }
}
