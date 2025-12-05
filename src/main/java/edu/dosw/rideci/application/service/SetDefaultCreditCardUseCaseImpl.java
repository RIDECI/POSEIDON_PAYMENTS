package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.SetDefaultCreditCardUseCase;
import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.domain.model.CreditCard;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SetDefaultCreditCardUseCaseImpl implements SetDefaultCreditCardUseCase {

    private final CreditCardRepositoryPort repo;

    @Override
    public CreditCard setDefault(String id) {

        var card = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        repo.clearDefaults(card.getUserId());

        card.setDefault(true);

        return repo.save(card);
    }
}
