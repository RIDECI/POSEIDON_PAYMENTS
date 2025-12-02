package edu.dosw.rideci.unit.controller;


import edu.dosw.rideci.application.port.in.*;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import edu.dosw.rideci.infrastructure.controller.PaymentController;
import edu.dosw.rideci.infrastructure.controller.dto.Request.CreatePaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Request.UpdatePaymentRequest;
import edu.dosw.rideci.infrastructure.controller.dto.Response.TransactionResponse;
import edu.dosw.rideci.infrastructure.controller.dto.Response.PaymentStatusResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock private CreatePaymentUseCase createPaymentUseCase;
    @Mock private ProcessPaymentUseCase processPaymentUseCase;
    @Mock private GetPaymentUseCase getPaymentUseCase;
    @Mock private DeletePaymentUseCase deletePaymentUseCase;
    @Mock private UpdatePaymentUseCase updatePaymentUseCase;
    @Mock private AuthorizePaymentUseCase authorizePaymentUseCase;
    @Mock private ApprovePaymentUseCase approvePaymentUseCase;
    @Mock private CompletePaymentUseCase completePaymentUseCase;
    @Mock private CancelPaymentUseCase cancelPaymentUseCase;
    @Mock private GetPaymentStatusUseCase getPaymentStatusUseCase;
    @Mock private GetPaymentsByTripUseCase getPaymentsByTripUseCase;
    @Mock private GetPaymentsByUserUseCase getPaymentsByUserUseCase;
    @Mock private GetPaymentsByDateUseCase getPaymentsByDateUseCase;
    @Mock private GetActivePaymentsUseCase getActivePaymentsUseCase;

    @InjectMocks
    private PaymentController controller;

    private Transaction transaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        transaction = Transaction.builder()
                .id("PAY-1")
                .bookingId("BKG-1")
                .passengerId("USR-1")
                .amount(15000.0)
                .status(TransactionStatus.PENDING)
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .build();
    }

    @Test
    void createPayment_ReturnsOk() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        when(createPaymentUseCase.create(any())).thenReturn(transaction);

        ResponseEntity<TransactionResponse> response = controller.create(request);

        assertEquals(200, response.getStatusCode().value());
        verify(createPaymentUseCase).create(any());
    }

    @Test
    void getPaymentById_Found() {
        when(getPaymentUseCase.getById("PAY-1"))
                .thenReturn(Optional.of(transaction));

        ResponseEntity<TransactionResponse> response = controller.getById("PAY-1");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getPaymentById_NotFound() {
        when(getPaymentUseCase.getById("PAY-1"))
                .thenReturn(Optional.empty());

        ResponseEntity<TransactionResponse> response = controller.getById("PAY-1");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getPaymentStatus_ReturnsOk() {
        when(getPaymentUseCase.getById("PAY-1"))
                .thenReturn(Optional.of(transaction));

        ResponseEntity<PaymentStatusResponse> response =
                controller.getPaymentStatus("PAY-1");

        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void getPaymentStatus_NotFound() {
        when(getPaymentUseCase.getById("PAY-1"))
                .thenReturn(Optional.empty());

        ResponseEntity<PaymentStatusResponse> response =
                controller.getPaymentStatus("PAY-1");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getPaymentsByTrip_NoContent() {
        when(getPaymentsByTripUseCase.getByTripId("TRIP"))
                .thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByTrip("TRIP");

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void getPaymentsByTrip_ReturnsOk() {
        when(getPaymentsByTripUseCase.getByTripId("TRIP"))
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByTrip("TRIP");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void getPaymentsByUser_NoContent() {
        when(getPaymentsByUserUseCase.getByUserId("USR"))
                .thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByUser("USR");

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void getPaymentsByUser_ReturnsOk() {
        when(getPaymentsByUserUseCase.getByUserId("USR"))
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByUser("USR");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void findAll_NoContent() {
        when(getPaymentUseCase.findAll()).thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> res =
                controller.findAll();

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void findAll_ReturnsOk() {
        when(getPaymentUseCase.findAll())
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> res =
                controller.findAll();

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void getPaymentsByDate_InvalidDate_ReturnsBadRequest() {
        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByDate("2025-99-99");

        assertEquals(400, res.getStatusCode().value());
    }

    @Test
    void getPaymentsByDate_NoContent() {
        when(getPaymentsByDateUseCase.getByDate(LocalDate.parse("2025-01-01")))
                .thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> res =
                controller.getPaymentsByDate("2025-01-01");

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void getActivePayments_ReturnsOk() {
        when(getActivePaymentsUseCase.getActivePayments())
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> res =
                controller.getActivePayments();

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void deletePayment_Found_ReturnsNoContent() {
        when(deletePaymentUseCase.deleteById("PAY-1")).thenReturn(true);

        ResponseEntity<Void> res = controller.deletePayment("PAY-1");

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void deletePayment_NotFound_ReturnsNotFound() {
        when(deletePaymentUseCase.deleteById("PAY-1")).thenReturn(false);

        ResponseEntity<Void> res = controller.deletePayment("PAY-1");

        assertEquals(404, res.getStatusCode().value());
    }

    @Test
    void updatePayment_ReturnsOk() {
        UpdatePaymentRequest req = new UpdatePaymentRequest();
        when(updatePaymentUseCase.updatePartial(eq("PAY-1"), any()))
                .thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.updatePayment("PAY-1", req);

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void authorizePayment_ReturnsOk() {
        when(authorizePaymentUseCase.authorize("PAY-1", true)).thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.authorizePayment("PAY-1", true);

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void processPayment_ReturnsOk() {
        when(processPaymentUseCase.process("PAY-1")).thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.processPayment("PAY-1");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void approvePayment_ReturnsOk() {
        when(approvePaymentUseCase.approve("PAY-1")).thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.approvePayment("PAY-1");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void completePayment_ReturnsOk() {
        when(completePaymentUseCase.complete("PAY-1")).thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.completePayment("PAY-1");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void cancelPayment_ReturnsOk() {
        when(cancelPaymentUseCase.cancel("PAY-1")).thenReturn(transaction);

        ResponseEntity<TransactionResponse> res =
                controller.cancelPayment("PAY-1");

        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void getByStatus_InvalidStatus_ReturnsBadRequest() {
        ResponseEntity<List<TransactionResponse>> res =
                controller.getByStatus("INVALID");

        assertEquals(400, res.getStatusCode().value());
    }

    @Test
    void getByStatus_NoContent() {
        when(getPaymentStatusUseCase.findByStatus(TransactionStatus.PENDING))
                .thenReturn(List.of());

        ResponseEntity<List<TransactionResponse>> res =
                controller.getByStatus("PENDING");

        assertEquals(204, res.getStatusCode().value());
    }

    @Test
    void getByStatus_ReturnsOk() {
        when(getPaymentStatusUseCase.findByStatus(TransactionStatus.PENDING))
                .thenReturn(List.of(transaction));

        ResponseEntity<List<TransactionResponse>> res =
                controller.getByStatus("PENDING");

        assertEquals(200, res.getStatusCode().value());
    }
}
