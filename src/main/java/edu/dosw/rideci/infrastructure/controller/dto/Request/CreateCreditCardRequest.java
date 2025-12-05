package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.CreditCard;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCreditCardRequest {

    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String cardHolder;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String expiration;

    @NotBlank
    private String cvv;

    private String alias;

    public CreditCard toDomain() {
        return CreditCard.builder()
                .id(id)
                .userId(userId)
                .cardHolder(cardHolder)
                .cardNumber(cardNumber)
                .expiration(expiration)
                .cvv(cvv)
                .alias(alias)
                .isDefault(false)
                .isActive(true)
                .build();
    }
}
