package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.CreditCard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardResponse {

    private String id;
    private String userId;
    private String alias;
    private String cardHolder;
    private String cardNumber; 
    private String expiration;
    private boolean isDefault;
    private boolean isActive;

    public static CreditCardResponse fromDomain(CreditCard c) {
        return CreditCardResponse.builder()
                .id(c.getId())
                .userId(c.getUserId())
                .alias(c.getAlias())
                .cardHolder(c.getCardHolder())
                .cardNumber(mask(c.getCardNumber()))
                .expiration(c.getExpiration())
                .isDefault(c.isDefault())
                .isActive(c.isActive())
                .build();
    }

    private static String mask(String num) {
        return "**** **** **** " + num.substring(num.length() - 4);
    }
}
