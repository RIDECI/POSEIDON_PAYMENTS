package edu.dosw.rideci.infrastructure.controller.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookUpdateRequest {
    private String status;
    private String receiptCode;
    private String errorMessage;
    private String metadata;
}
