package edu.dosw.rideci.application.service;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.DeleteCreditCardUseCase;
import edu.dosw.rideci.application.port.out.CreditCardRepositoryPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCreditCardUseCaseImpl implements DeleteCreditCardUseCase {

    private final CreditCardRepositoryPort repo;

    @Override
    public boolean delete(String id) {
        if (repo.findById(id).isEmpty()) return false;

        repo.deleteById(id);
        return true;
    }
}
