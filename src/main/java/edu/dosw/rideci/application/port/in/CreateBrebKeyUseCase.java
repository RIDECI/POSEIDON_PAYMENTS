package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.BrebKey;

public interface CreateBrebKeyUseCase {
    BrebKey create(BrebKey key);
}
