package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.domain.model.CreditCard;
import edu.dosw.rideci.infrastructure.persistence.Entity.CreditCardEntity;

@Component
public class CreditCardMapper {

    public CreditCardEntity toEntity(CreditCard domain) {
        return CreditCardEntity.builder()
                .id(domain.getId())
                .userId(domain.getUserId())
                .cardHolder(domain.getCardHolder())
                .cardNumber(domain.getCardNumber())
                .expiration(domain.getExpiration())
                .cvv(domain.getCvv())
                .alias(domain.getAlias())
                .isDefault(domain.isDefault())
                .isActive(domain.isActive())
                .build();
    }

    public CreditCard toDomain(CreditCardEntity entity) {
        return CreditCard.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .cardHolder(entity.getCardHolder())
                .cardNumber(entity.getCardNumber())
                .expiration(entity.getExpiration())
                .cvv(entity.getCvv())
                .alias(entity.getAlias())
                .isDefault(entity.isDefault())
                .isActive(entity.isActive())
                .build();
    }
}
