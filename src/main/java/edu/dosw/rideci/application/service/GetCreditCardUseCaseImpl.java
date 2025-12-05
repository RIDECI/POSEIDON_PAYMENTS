package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetCreditCardUseCase;
import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import edu.dosw.rideci.domain.model.CreditCard;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetCreditCardUseCaseImpl implements GetCreditCardUseCase {

    private final CreditCardRepositoryPort repo;

    @Override
    public List<CreditCard> findAll() {
        return repo.findAll();
    }

    @Override
    public List<CreditCard> findByUser(String userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public CreditCard findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
    }

    @Override
    public CreditCard findDefault(String userId) {
        return repo.findDefaultForUser(userId)
                .orElseThrow(() -> new RuntimeException("No hay tarjeta predeterminada"));
    }
}
