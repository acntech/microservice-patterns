package no.acntech.reservation.consumer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.DeleteReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import no.acntech.reservation.service.ReservationOrchestrationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@Component
public class ReservationRabbitConsumer {

    private final ReservationOrchestrationService reservationOrchestrationService;

    public ReservationRabbitConsumer(final ReservationOrchestrationService reservationOrchestrationService) {
        this.reservationOrchestrationService = reservationOrchestrationService;
    }

    @RabbitListener(queues = RabbitQueue.CREATE_RESERVATION)
    public void consumeCreateReservation(@NotNull @Valid final CreateReservationDto createReservationDto) {
        reservationOrchestrationService.createReservation(createReservationDto);
    }

    @RabbitListener(queues = RabbitQueue.UPDATE_RESERVATION)
    public void consumeUpdateReservation(@NotNull @Valid final UpdateReservationDto updateReservationDto) {
        reservationOrchestrationService.updateReservation(updateReservationDto);
    }

    @RabbitListener(queues = RabbitQueue.DELETE_RESERVATION)
    public void consumeDeleteReservation(@NotNull @Valid final DeleteReservationDto deleteReservationDto) {
        reservationOrchestrationService.deleteReservation(deleteReservationDto);
    }
}
