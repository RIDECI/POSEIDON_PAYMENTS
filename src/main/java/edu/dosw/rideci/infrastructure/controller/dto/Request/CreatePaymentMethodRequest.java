package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePaymentMethodRequest {

    private String id; 

    @NotBlank
    private String userId;

    private String alias;

    @NotNull
    private PaymentMethodType type;

    public PaymentMethod toDomain() {
        return PaymentMethod.builder()
                .id(id)
                .userId(userId)
                .alias(alias)
                .isDefault(false)
                .isActive(true)
                .type(type)
                .build();
    }
}
