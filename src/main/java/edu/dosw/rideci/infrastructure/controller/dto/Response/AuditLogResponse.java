package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.AuditLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {
    private String id;
    private String entityType;
    private String entityId;
    private String action;
    private String userId;
    private String userName;
    private String description;
    private String previousState;
    private String newState;
    private String ipAddress;
    private LocalDateTime timestamp;

    public static AuditLogResponse fromDomain(AuditLog audit) {
        if (audit == null) {
            return null;
        }
        return AuditLogResponse.builder()
                .id(audit.getId())
                .entityType(audit.getEntityType())
                .entityId(audit.getEntityId())
                .action(audit.getAction() != null ? audit.getAction().name() : null)
                .userId(audit.getUserId())
                .userName(audit.getUserName())
                .description(audit.getDescription())
                .previousState(audit.getPreviousState())
                .newState(audit.getNewState())
                .ipAddress(audit.getIpAddress())
                .timestamp(audit.getTimestamp())
                .build();
    }
}
