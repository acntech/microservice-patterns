package no.acntech.common.config;

public interface RabbitQueue {

    String CREATE_RESERVATION = "messaging-create-reservation";
    String UPDATE_RESERVATION = "messaging-update-reservation";
    String DELETE_RESERVATION = "messaging-delete-reservation";
    String UPDATE_ORDER_ITEM = "messaging-update-order-item";
}
