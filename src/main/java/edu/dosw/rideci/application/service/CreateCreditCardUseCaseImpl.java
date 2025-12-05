package edu.dosw.rideci.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.CreateCreditCardUseCase;
import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.domain.model.CreditCard;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateCreditCardUseCaseImpl implements CreateCreditCardUseCase {

    private final CreditCardRepositoryPort repo;

    @Override
    public CreditCard create(CreditCard card) {

        card.setId("CC-" + UUID.randomUUID());
        card.setActive(true);

        if (repo.findByUserId(card.getUserId()).isEmpty()) {
            card.setDefault(true);
        }

        return repo.save(card);
    }
}
