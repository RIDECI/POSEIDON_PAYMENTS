package edu.dosw.rideci.infrastructure.persistence.Entity;

import edu.dosw.rideci.domain.model.enums.BrebKeyType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "breb_keys")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrebKeyEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    private BrebKeyType type;

    @Column(name = "is_default")
    private boolean isDefault;
}
