package no.acntech.common.config;

public interface RabbitQueue {

    String UPDATE_ORDER_ITEM = "messaging-update-order-item";
    String CREATE_INVOICE = "messaging-create-invoice";
    String CREATE_SHIPMENT = "messaging-create-shipment";
    String CREATE_RESERVATION = "messaging-create-reservation";
    String UPDATE_RESERVATION = "messaging-update-reservation";
    String DELETE_RESERVATION = "messaging-delete-reservation";
}
