package no.acntech.reservation.service;

import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.service.OrderOrchestrationService;
import no.acntech.reservation.model.ReservationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReservationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);

    private final ConversionService conversionService;
    private final OrderOrchestrationService orderOrchestrationService;

    public ReservationService(final ConversionService conversionService,
                              final OrderOrchestrationService orderOrchestrationService) {
        this.conversionService = conversionService;
        this.orderOrchestrationService = orderOrchestrationService;
    }

    public void processReservationEvent(final ReservationEvent reservationEvent) {
        try {
            switch (reservationEvent.getEventType()) {
                case RESERVATION_CREATED, RESERVATION_CONFIRMED, RESERVATION_REJECTED, RESERVATION_FAILED ->
                        processReservation(reservationEvent);
                default ->
                        LOGGER.debug("Ignoring reservation event with type {} for reservation-id {}", reservationEvent.getEventType(), reservationEvent.getReservationId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing reservation event", e);
        }
    }

    private void processReservation(final ReservationEvent reservationEvent) {
        final var updateOrderItemDto = conversionService.convert(reservationEvent, UpdateOrderItemDto.class);
        Assert.notNull(updateOrderItemDto, "Failed to ReservationEvent OrderDto to UpdateOrderItemDto");
        orderOrchestrationService.updateOrderItem(updateOrderItemDto);
    }
}
