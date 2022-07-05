package no.acntech.reservation.service;

import no.acntech.order.model.UpdateOrderItemReservationDto;
import no.acntech.order.producer.OrderRabbitProducer;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.DeleteReservationDto;
import no.acntech.reservation.model.ReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationOrchestrationService {

    private final ConversionService conversionService;
    private final ReservationService reservationService;
    private final OrderRabbitProducer orderRabbitProducer;

    public ReservationOrchestrationService(final ConversionService conversionService,
                                           final ReservationService reservationService,
                                           final OrderRabbitProducer orderRabbitProducer) {
        this.conversionService = conversionService;
        this.reservationService = reservationService;
        this.orderRabbitProducer = orderRabbitProducer;
    }

    public ReservationDto getReservation(@NotNull final UUID reservationId) {
        return reservationService.getReservation(reservationId);
    }

    public List<ReservationDto> findReservations(final UUID orderId) {
        return reservationService.findReservations(orderId);
    }

    @Transactional
    public void createReservation(@NotNull @Valid final CreateReservationDto createReservationDto) {
        final var reservationDto = reservationService.createReservation(createReservationDto);
        final var updateOrderItemReservationDto = convert(reservationDto);
        orderRabbitProducer.updateOrderItem(updateOrderItemReservationDto);
    }

    @Transactional
    public void updateReservation(@NotNull @Valid final UpdateReservationDto updateReservation) {
        final var reservationDto = reservationService.updateReservation(updateReservation);
        final var updateOrderItemReservationDto = convert(reservationDto);
        orderRabbitProducer.updateOrderItem(updateOrderItemReservationDto);
    }

    @Transactional
    public void deleteReservation(@NotNull @Valid final DeleteReservationDto deleteReservationDto) {
        final var reservationDto = reservationService.deleteReservation(deleteReservationDto);
        final var updateOrderItemReservationDto = convert(reservationDto);
        orderRabbitProducer.updateOrderItem(updateOrderItemReservationDto);
    }

    private UpdateOrderItemReservationDto convert(final ReservationDto source) {
        final var target = conversionService.convert(source, UpdateOrderItemReservationDto.class);
        Assert.notNull(target, "Failed to convert ReservationDto to UpdateOrderItemReservationDto");
        return target;
    }
}
