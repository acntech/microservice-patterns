package no.acntech.shipping.model;

public class SendShipmentMessage {

    private Long orderId;
    private String reservationId;

    public SendShipmentMessage(Long orderId, String reservationId) {
        this.orderId = orderId;
        this.reservationId = reservationId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getReservationId() {
        return reservationId;
    }
}
