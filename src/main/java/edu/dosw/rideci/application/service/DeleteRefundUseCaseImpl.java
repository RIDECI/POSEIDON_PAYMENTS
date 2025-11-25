package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.DeleteRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRefundUseCaseImpl implements DeleteRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public boolean deleteById(String id) {

        if (!refundRepositoryPort.existsById(id)) {
            return false; // igual que tu delete de pagos
        }

        refundRepositoryPort.deleteById(id);
        return true;
    }
}
