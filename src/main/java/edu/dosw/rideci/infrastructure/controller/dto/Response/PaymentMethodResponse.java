package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodResponse {

    private String id;
    private String userId;
    private String alias;
    private boolean isDefault;
    private boolean isActive;
    private PaymentMethodType type;

    public static PaymentMethodResponse fromDomain(PaymentMethod pm) {
        if (pm == null) return null;
        return PaymentMethodResponse.builder()
                .id(pm.getId())
                .userId(pm.getUserId())
                .alias(pm.getAlias())
                .isDefault(pm.isDefault())
                .isActive(pm.isActive())
                .type(pm.getType())
                .build();
    }
}
