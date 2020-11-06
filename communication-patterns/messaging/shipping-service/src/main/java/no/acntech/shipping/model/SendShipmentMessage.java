package no.acntech.shipping.model;

public class SendShipmentMessage {

    private Long orderId;
    private String reservationId;

    public Long getOrderId() {
        return orderId;
    }

    public String getReservationId() {
        return reservationId;
    }
}
