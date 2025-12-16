package edu.dosw.rideci.infrastructure.persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Table(name = "credit_cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "card_holder", nullable = false)
    private String cardHolder;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expiration", nullable = false) 
    private String expiration;

    @Column(nullable = false)
    private String cvv; 

    private String alias;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "is_active")
    private boolean isActive;
}
