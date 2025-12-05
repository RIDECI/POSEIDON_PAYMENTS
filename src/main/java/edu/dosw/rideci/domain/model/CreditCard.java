package edu.dosw.rideci.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

    private String id;
    private String userId;
    private String cardHolder;
    private String cardNumber;
    private String expiration;
    private String cvv;
    private String alias;
    private boolean isDefault;
    private boolean isActive;
}
