package edu.dosw.rideci.unit.usecases;


import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.application.service.AuthorizePaymentUseCaseImpl;
import edu.dosw.rideci.domain.model.Transaction;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import edu.dosw.rideci.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizePaymentUseCaseImplTest {

    @Mock
    private AuthorizePaymentPort authorizePaymentPort;

    @InjectMocks
    private AuthorizePaymentUseCaseImpl authorizePaymentUseCase;

    private Transaction transaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        transaction = Transaction.builder()
                .id("TX-123")
                .bookingId("BKG-100")
                .paymentMethod(PaymentMethodType.CREDIT_CARD_PAYU)
                .status(TransactionStatus.PROCESSING)
                .amount(18000.0)
                .build();
    }

    @Test
    void shouldAuthorizePaymentSuccessfully() {
        when(authorizePaymentPort.authorizePayment("TX-123", true))
                .thenReturn(Optional.of(transaction));

        Transaction result = authorizePaymentUseCase.authorize("TX-123", true);

        assertNotNull(result);
        assertEquals("TX-123", result.getId());
        verify(authorizePaymentPort).authorizePayment("TX-123", true);
    }

    @Test
    void shouldThrowExceptionWhenAuthorizationFails() {
        when(authorizePaymentPort.authorizePayment("TX-123", true))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authorizePaymentUseCase.authorize("TX-123", true));

        assertEquals("Authorization failed", ex.getMessage());
        verify(authorizePaymentPort).authorizePayment("TX-123", true);
    }
}
