package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {
    private String id; 
    private String userId;
    private String alias;
    private boolean isDefault;
    private boolean isActive;
    private PaymentMethodType type;  
    
    public boolean isInvalid(){
        return isActive;
    }
}
