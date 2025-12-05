package edu.dosw.rideci.application.port.in;

import java.util.List;

import edu.dosw.rideci.domain.model.BrebKey;

public interface GetBrebKeyUseCase {

    List<BrebKey> findAll();

    List<BrebKey> findByUser(String userId);

    BrebKey findById(String id);

    BrebKey findDefault(String userId);
}
