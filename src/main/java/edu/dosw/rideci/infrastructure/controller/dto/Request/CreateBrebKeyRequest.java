package edu.dosw.rideci.infrastructure.controller.dto.Request;

import edu.dosw.rideci.domain.model.BrebKey;
import edu.dosw.rideci.domain.model.enums.BrebKeyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBrebKeyRequest {

    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String value;

    @NotNull
    private BrebKeyType type;

    public BrebKey toDomain() {
        return BrebKey.builder()
                .id(id)
                .userId(userId)
                .value(value)
                .type(type)
                .isDefault(false)
                .build();
    }
}
