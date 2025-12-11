package edu.dosw.rideci.infrastructure.controller;

import edu.dosw.rideci.application.port.in.GenerateReceiptPdfUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/receipt")
public class PaymentReceiptPdfController {

    private final GenerateReceiptPdfUseCase pdfUseCase;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> download(@PathVariable String id) {

        byte[] pdf = pdfUseCase.generatePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
