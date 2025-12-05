package edu.dosw.rideci.domain.model;

import edu.dosw.rideci.domain.model.enums.BrebKeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrebKey {
    private String id;
    private String userId;
    private String value;
    private BrebKeyType type;
    private boolean isDefault;
}
