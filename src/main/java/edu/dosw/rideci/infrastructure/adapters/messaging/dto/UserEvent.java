package edu.dosw.rideci.infrastructure.adapters.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {
    private String id;
    private Long userId;
    private String name;
    private String email;
    private String state; // ACTIVE, BLOCKED, DELETED, SUSPENDED, etc.
    private String role; // PASSENGER, DRIVER, ADMIN, etc.
    private String timestamp;
}
