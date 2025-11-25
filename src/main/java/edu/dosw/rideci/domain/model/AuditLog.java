package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditLog {
    private String id;
    private String entityType;
    private String entityId;
    private AuditAction action;
    private String userId;
    private String userName;
    private String description;
    private String previousState;
    private String newState;
    private String ipAddress;
    private LocalDateTime timestamp;
}
