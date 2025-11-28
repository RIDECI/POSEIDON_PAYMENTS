package edu.dosw.rideci.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.dosw.rideci.application.port.in.ApproveRefundUseCase;
import edu.dosw.rideci.application.port.in.AuthorizeRefundUseCase;
import edu.dosw.rideci.application.port.in.CompleteRefundUseCase;
import edu.dosw.rideci.application.port.in.ProcessRefundUseCase;
import edu.dosw.rideci.infrastructure.controller.dto.Response.RefundResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class RefundWorkflowController {

    private final AuthorizeRefundUseCase authorizeUseCase;
    private final ProcessRefundUseCase processUseCase;
    private final ApproveRefundUseCase approveUseCase;
    private final CompleteRefundUseCase completeUseCase;

    @PatchMapping("/{id}/authorizeR")
    public ResponseEntity<RefundResponse> authorize(@PathVariable String id) {
        return ResponseEntity.ok(
                RefundResponse.fromDomain(authorizeUseCase.authorize(id))
        );
    }

    @PatchMapping("/{id}/processR")
    public ResponseEntity<RefundResponse> process(@PathVariable String id) {
        return ResponseEntity.ok(
                RefundResponse.fromDomain(processUseCase.process(id))
        );
    }

    @PatchMapping("/{id}/approveR")
    public ResponseEntity<RefundResponse> approve(@PathVariable String id) {
        return ResponseEntity.ok(
                RefundResponse.fromDomain(approveUseCase.approve(id))
        );
    }

    @PatchMapping("/{id}/completeR")
    public ResponseEntity<RefundResponse> complete(@PathVariable String id) {
        return ResponseEntity.ok(
                RefundResponse.fromDomain(completeUseCase.complete(id))
        );
    }
}

