package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.domain.model.CashPaymentConfirmation;
import edu.dosw.rideci.infrastructure.persistence.Entity.CashPaymentConfirmationEntity;

@Component
public class CashPaymentConfirmationMapper {

    public CashPaymentConfirmationEntity toEntity(CashPaymentConfirmation domain) {
        return CashPaymentConfirmationEntity.builder()
                .id(domain.getId())
                .transactionId(domain.getTransactionId())
                .bookingId(domain.getBookingId())
                .driverId(domain.getDriverId())
                .passengerId(domain.getPassengerId())
                .amount(domain.getAmount())
                .confirmed(domain.isConfirmed())
                .confirmedAt(domain.getConfirmedAt())
                .observations(domain.getObservations())
                .build();
    }

    public CashPaymentConfirmation toDomain(CashPaymentConfirmationEntity entity) {
        return CashPaymentConfirmation.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .bookingId(entity.getBookingId())
                .driverId(entity.getDriverId())
                .passengerId(entity.getPassengerId())
                .amount(entity.getAmount())
                .confirmed(entity.isConfirmed())
                .confirmedAt(entity.getConfirmedAt())
                .observations(entity.getObservations())
                .build();
    }
}
