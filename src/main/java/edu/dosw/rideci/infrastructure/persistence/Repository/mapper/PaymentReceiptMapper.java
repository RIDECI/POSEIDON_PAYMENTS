package edu.dosw.rideci.infrastructure.persistence.Repository.mapper;

import org.springframework.stereotype.Component;

import edu.dosw.rideci.domain.model.PaymentReceipt;
import edu.dosw.rideci.infrastructure.persistence.Entity.PaymentReceiptEntity;

@Component
public class PaymentReceiptMapper {

    public PaymentReceiptEntity toEntity(PaymentReceipt domain) {
        return PaymentReceiptEntity.builder()
                .id(domain.getId())
                .transactionId(domain.getTransactionId())
                .receiptCode(domain.getReceiptCode())
                .passengerId(domain.getPassengerId())
                .driverId(domain.getDriverId())
                .bookingId(domain.getBookingId())
                .amount(domain.getAmount())
                .paymentMethod(domain.getPaymentMethod())
                .transactionMethod(domain.getTransactionMethod())
                .paymentDetails(domain.getPaymentDetails())
                .issuedAt(domain.getIssuedAt())
                .downloadUrl(domain.getDownloadUrl())
                .build();
    }

    public PaymentReceipt toDomain(PaymentReceiptEntity entity) {
        return PaymentReceipt.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .receiptCode(entity.getReceiptCode())
                .passengerId(entity.getPassengerId())
                .driverId(entity.getDriverId())
                .bookingId(entity.getBookingId())
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod())
                .transactionMethod(entity.getTransactionMethod())
                .paymentDetails(entity.getPaymentDetails())
                .issuedAt(entity.getIssuedAt())
                .downloadUrl(entity.getDownloadUrl())
                .build();
    }
}
