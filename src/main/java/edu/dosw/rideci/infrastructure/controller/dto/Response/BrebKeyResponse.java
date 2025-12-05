package edu.dosw.rideci.infrastructure.controller.dto.Response;

import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.domain.model.enums.BrebKeyType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrebKeyResponse {

    private String id;
    private String userId;
    private String value;
    private BrebKeyType type;
    private boolean isDefault;

    public static BrebKeyResponse fromDomain(BrebKey key) {
        return BrebKeyResponse.builder()
                .id(key.getId())
                .userId(key.getUserId())
                .value(key.getValue())
                .type(key.getType())
                .isDefault(key.isDefault())
                .build();
    }
}
