package edu.dosw.rideci.infrastructure.adapters.messaging.listener;

import edu.dosw.rideci.infrastructure.adapters.messaging.dto.LocationDto;
import edu.dosw.rideci.infrastructure.adapters.messaging.dto.TravelCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelCreatedEventListener {

    @RabbitListener(queues = "geolocation.travel.created.queue")
    public void handleTravelCreated(TravelCreatedEvent event) {

        if (event == null) {
            log.error("❌ Received null TravelCreatedEvent");
            return;
        }

        log.info("TRAVEL CREATED EVENT RECEIVED in PAYMENTS module");
        log.info("Travel ID: {}", event.getTravelId());
        log.info("User ID: {}", event.getUserId());
        log.info("Driver ID: {}", event.getDriverId());
        log.info("Estimated Fare: {}", event.getEstimatedFare());

        if (event.getOrigin() == null || event.getDestiny() == null) {
            log.error("❌ Origin or Destiny is null for travelId={}", event.getTravelId());
            return;
        }

        logLocation("Origin", event.getOrigin());
        logLocation("Destiny", event.getDestiny());

        try {
            log.info("💳 Travel created - Payment will be processed when travel completes");
            log.info("✅ Travel event processed for travel: {}", event.getTravelId());
        } catch (Exception ex) {
            log.error("❌ Failed to process payment for travelId={}", event.getTravelId(), ex);
        }
    }

    private void logLocation(String label, LocationDto location) {
        log.info("{}: {} ({}, {})",
                label,
                location.getDirection(),
                location.getLatitude(),
                location.getLongitude());
    }
}
