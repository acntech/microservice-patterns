package no.acntech.reservation.service;

import no.acntech.order.model.UpdateOrderItemDto;
import no.acntech.order.service.OrderOrchestrationService;
import no.acntech.reservation.consumer.ReservationRestConsumer;
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
    private final ReservationRestConsumer reservationRestConsumer;
    private final OrderOrchestrationService orderOrchestrationService;

    public ReservationService(final ConversionService conversionService,
                              final ReservationRestConsumer reservationRestConsumer,
                              final OrderOrchestrationService orderOrchestrationService) {
        this.conversionService = conversionService;
        this.reservationRestConsumer = reservationRestConsumer;
        this.orderOrchestrationService = orderOrchestrationService;
    }

    public void processReservationEvent(final ReservationEvent reservationEvent) {
        LOGGER.debug("Processing ReservationEvent for reservation-id {}", reservationEvent.getReservationId());

        try {
            LOGGER.debug("Retrieving ReservationDto for reservation-id {}", reservationEvent.getReservationId());
            final var optionalReservationDto = reservationRestConsumer.get(reservationEvent.getReservationId());
            if (optionalReservationDto.isPresent()) {
                final var reservationDto = optionalReservationDto.get();
                final var updateOrderItemDto = conversionService.convert(reservationDto, UpdateOrderItemDto.class);
                Assert.notNull(updateOrderItemDto, "Failed to ReservationDto to UpdateOrderItemDto");
                orderOrchestrationService.updateOrderItemReservation(updateOrderItemDto);
            } else {
                LOGGER.error("Reservation with reservation-id {} could not be found", reservationEvent.getReservationId());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while processing reservation event", e);
        }
    }
}
