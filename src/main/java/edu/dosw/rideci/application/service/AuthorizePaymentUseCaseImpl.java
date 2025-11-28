package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.AuthorizePaymentUseCase;
import edu.dosw.rideci.application.port.out.AuthorizePaymentPort;
import edu.dosw.rideci.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizePaymentUseCaseImpl implements AuthorizePaymentUseCase {

    private final AuthorizePaymentPort authorizationPort;

    @Override
    public Transaction authorize(String id, boolean authorized) {
        return authorizationPort.authorizePayment(id, authorized)
                .orElseThrow(() -> new RuntimeException("Authorization failed"));
    }
}
