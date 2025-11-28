package edu.dosw.rideci.infrastructure.persistence.Entity;

import edu.dosw.rideci.domain.model.enums.AuditAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {

    @Id
    private String id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "previous_state", columnDefinition = "TEXT")
    private String previousState;

    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        if (id == null)
            id = UUID.randomUUID().toString();
        if (timestamp == null)
            timestamp = LocalDateTime.now();
    }

}
