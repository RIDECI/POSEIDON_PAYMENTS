package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.in.CreateAuditLogUseCase;
import edu.dosw.rideci.application.port.out.PaymentApprovalPort;
import edu.dosw.rideci.application.port.out.PaymentRepositoryPort;
import edu.dosw.rideci.application.service.ApprovePaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.AuditLog;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApprovePaymentUseCaseImplTest {

    @Mock
    private PaymentRepositoryPort paymentRepositoryPort;

    @Mock
    private PaymentApprovalPort approvalPort;

    @Mock
    private CreateAuditLogUseCase createAuditLogUseCase;

    @InjectMocks
    private ApprovePaymentUseCaseImpl approvePaymentUseCase;

    private Transaction tx;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        tx = new Transaction();
        tx.setId("TX-001");
        tx.setStatus(TransactionStatus.PROCESSING);
        tx.setReceiptCode("123456");
        tx.setPaymentMethod(PaymentMethodType.CREDIT_CARD_PAYU);
        tx.setAmount(1000.0);
        tx.setBookingId("BOOKING-123");
    }

    @Test
    void shouldApprovePaymentSuccessfully() {
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        when(paymentRepositoryPort.save(any())).thenReturn(tx);

        Transaction result = approvePaymentUseCase.approve("TX-001");

        assertEquals(TransactionStatus.APPROVED, result.getStatus());

        verify(approvalPort, times(1)).logApproval("TX-001");
        verify(createAuditLogUseCase, times(1)).createAuditLog(any(AuditLog.class));
        verify(paymentRepositoryPort, times(1)).save(tx);
    }

    @Test
    void shouldThrowIfTransactionNotFound() {
        when(paymentRepositoryPort.findById("INVALID")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> approvePaymentUseCase.approve("INVALID"));
    }

    @Test
    void shouldFailIfNotProcessing() {
        tx.setStatus(TransactionStatus.PENDING);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalStateException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }

    @Test
    void shouldFailIfReceiptIsMissing() {
        tx.setReceiptCode(null);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalArgumentException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }

    @Test
    void shouldFailIfPaymentMethodIsCash() {
        tx.setPaymentMethod(PaymentMethodType.CASH);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalStateException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }

    @Test
    void shouldFailIfAlreadyAuthorized() {
        tx.setStatus(TransactionStatus.AUTHORIZED);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalStateException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }

    @Test
    void shouldFailIfAmountInvalid() {
        tx.setAmount(0.0);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalStateException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }

    @Test
    void shouldFailIfBookingMissing() {
        tx.setBookingId(null);
        when(paymentRepositoryPort.findById("TX-001")).thenReturn(Optional.of(tx));
        assertThrows(IllegalArgumentException.class, () -> approvePaymentUseCase.approve("TX-001"));
    }
}
