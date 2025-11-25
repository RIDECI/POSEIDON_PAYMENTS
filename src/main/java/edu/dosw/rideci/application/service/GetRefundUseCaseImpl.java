package edu.dosw.rideci.application.service;

import edu.dosw.rideci.application.port.in.GetRefundUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetRefundUseCaseImpl implements GetRefundUseCase {

    private final RefundRepositoryPort refundRepositoryPort;

    @Override
    public Optional<Refund> getById(String id) {
        return refundRepositoryPort.findById(id);
    }

    @Override
    public List<Refund> getAll() {
        return refundRepositoryPort.findAll();
    }
}
