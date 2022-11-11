package no.acntech.reservation.producer;

import no.acntech.common.config.RabbitQueue;
import no.acntech.reservation.model.CreateReservationDto;
import no.acntech.reservation.model.DeleteReservationDto;
import no.acntech.reservation.model.UpdateReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;

@Component
public class ReservationRabbitProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRabbitProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public ReservationRabbitProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void create(@Valid final CreateReservationDto createReservationDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.CREATE_RESERVATION, createReservationDto);
        LOGGER.debug("Sent create reservation message for order with order-id {}", createReservationDto.getOrderId());
    }

    public void update(@Valid final UpdateReservationDto updateReservationDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.UPDATE_RESERVATION, updateReservationDto);
        LOGGER.debug("Sent update reservation message for reservation with reservation-id {}", updateReservationDto.getReservationId());
    }

    public void delete(@Valid final DeleteReservationDto deleteReservationDto) {
        rabbitTemplate.convertAndSend(RabbitQueue.DELETE_RESERVATION, deleteReservationDto);
        LOGGER.debug("Sent delete reservation message for reservation with reservation-id {}", deleteReservationDto.getReservationId());
    }
}
