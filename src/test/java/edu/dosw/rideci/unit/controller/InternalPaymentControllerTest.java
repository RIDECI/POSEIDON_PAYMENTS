package edu.dosw.rideci.unit.controller;


import edu.dosw.rideci.application.port.in.CancelTripPaymentsUseCase;
import edu.dosw.rideci.application.port.in.CompleteTripPaymentsUseCase;
import edu.dosw.rideci.application.port.in.VerifyPaymentApprovalUseCase;
import edu.dosw.rideci.application.port.in.RegisterPaymentFailureUseCase;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.controller.InternalPaymentController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.PaymentFailureRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentVerificationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InternalPaymentControllerTest {

    @Mock
    private CancelTripPaymentsUseCase cancelTripPaymentsUseCase;

    @Mock
    private CompleteTripPaymentsUseCase completeTripPaymentsUseCase;

    @Mock
    private VerifyPaymentApprovalUseCase verifyPaymentApprovalUseCase;

    @Mock
    private RegisterPaymentFailureUseCase registerPaymentFailureUseCase;

    @InjectMocks
    private InternalPaymentController controller;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transaction = Transaction.builder()
                .id("PAY-1")
                .bookingId("BKG-1")
                .passengerId("USR-1")
                .amount(18000.0)
                .status(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .receiptCode("ABC123")
                .build();
    }

    @Test
    void cancelTripPayments_ReturnsUpdatedTransactions() {
        when(cancelTripPaymentsUseCase.cancelTripPayments("TRIP-123"))
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> response =
                controller.cancelTripPayments("TRIP-123");

        assertEquals(200, response.getStatusCode().value());
        verify(cancelTripPaymentsUseCase).cancelTripPayments("TRIP-123");
    }

    @Test
    void cancelTripPayments_NoPayments_ReturnsNoContent() {
        when(cancelTripPaymentsUseCase.cancelTripPayments("TRIP-123"))
                .thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> response =
                controller.cancelTripPayments("TRIP-123");

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void completeTripPayments_ReturnsUpdatedTransactions() {
        when(completeTripPaymentsUseCase.completeTripPayments("TRIP-456"))
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> response =
                controller.completeTripPayments("TRIP-456");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void verifyPaymentApproval_Found_ReturnsResponse() {
        when(verifyPaymentApprovalUseCase.verifyApproval("PAY-1"))
                .thenReturn(Optional.of(transaction));

        ResponseEntity<PaymentVerificationResponse> response =
                controller.verifyPaymentApproval("PAY-1");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void verifyPaymentApproval_NotFound_ReturnsNotFound() {
        when(verifyPaymentApprovalUseCase.verifyApproval("PAY-1"))
                .thenReturn(Optional.empty());

        ResponseEntity<PaymentVerificationResponse> response =
                controller.verifyPaymentApproval("PAY-1");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void registerPaymentFailure_ReturnsUpdatedPayment() {
        PaymentFailureRequest req = new PaymentFailureRequest("PAY-1", "Error", "500");

        when(registerPaymentFailureUseCase.registerFailure("PAY-1", "Error", "500"))
                .thenReturn(transaction);

        ResponseEntity<TransactionResponse> response =
                controller.registerPaymentFailure(req);

        assertEquals(200, response.getStatusCode().value());
        verify(registerPaymentFailureUseCase).registerFailure("PAY-1", "Error", "500");
    }
}
