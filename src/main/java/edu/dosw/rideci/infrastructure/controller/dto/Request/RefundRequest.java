package edu.dosw.rideci.infrastructure.controller.dto.Request;

import lombok.Data;

@Data
public class RefundRequest {
    private Double amount;
    private String reason;
}
