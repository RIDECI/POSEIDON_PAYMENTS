package edu.dosw.rideci.unit.usecases;

import edu.dosw.rideci.application.port.out.RefundRepositoryPort;
import edu.dosw.rideci.application.service.GetRefundStatusUseCaseImpl;
import edu.dosw.rideci.domain.model.Refund;
import edu.dosw.rideci.domain.model.enums.RefundStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetRefundStatusUseCaseImplTest {

    private RefundRepositoryPort repo;
    private GetRefundStatusUseCaseImpl getRefundStatusUseCase;

    @BeforeEach
    void setUp() {
        repo = mock(RefundRepositoryPort.class);
        getRefundStatusUseCase = new GetRefundStatusUseCaseImpl(repo);
    }

    @Test
    void findByStatus_returnsList() {
        Refund r1 = Refund.builder().id("r1").status(RefundStatus.APPROVED).build();
        Refund r2 = Refund.builder().id("r2").status(RefundStatus.APPROVED).build();
        List<Refund> refunds = Arrays.asList(r1, r2);

        when(repo.findByStatus(RefundStatus.APPROVED)).thenReturn(refunds);

        List<Refund> result = getRefundStatusUseCase.findByStatus(RefundStatus.APPROVED);

        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
        verify(repo).findByStatus(RefundStatus.APPROVED);
    }

    @Test
    void findByStatus_emptyList() {
        when(repo.findByStatus(RefundStatus.REQUESTED)).thenReturn(List.of());

        List<Refund> result = getRefundStatusUseCase.findByStatus(RefundStatus.REQUESTED);

        assertTrue(result.isEmpty());
        verify(repo).findByStatus(RefundStatus.REQUESTED);
    }
}
