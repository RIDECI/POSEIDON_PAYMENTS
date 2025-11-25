package edu.dosw.rideci.infrastructure.adapters.persistence;

import edu.dosw.rideci.application.port.out.PaymentApprovalPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApprovePaymentAdapter implements PaymentApprovalPort {

    @Override
    public void logApproval(String transactionId) {
        log.info("Payment approved: " + transactionId);
    }
}
