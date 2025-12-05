package edu.dosw.rideci.application.port.in;

import edu.dosw.rideci.domain.model.BrebKey;

public interface SetDefaultBrebKeyUseCase {
    BrebKey setDefault(String id);
}
