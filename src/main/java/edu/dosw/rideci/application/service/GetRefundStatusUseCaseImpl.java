package edu.dosw.rideci.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.dosw.rideci.application.port.in.GetRefundStatusUseCase;
import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRefundStatusUseCaseImpl implements GetRefundStatusUseCase {

    private final RefundRepositoryPort repo;

    @Override
    public List<Refund> findByStatus(RefundStatus status) {
        return repo.findByStatus(status);
    }
}
