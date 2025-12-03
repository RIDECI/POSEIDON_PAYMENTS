package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.PaymentMethod;
import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.Data;

@Data
public class UpdatePaymentMethodRequest {

    private String alias;
    private Boolean isActive;
    private Boolean isDefault;
    private PaymentMethodType type;

    // Map partial into existing domain instance (helper)
    public void applyTo(PaymentMethod target) {
        if (alias != null) target.setAlias(alias);
        if (isActive != null) target.setActive(isActive);
        if (isDefault != null) target.setDefault(isDefault);
        if (type != null) target.setType(type);
    }
}
