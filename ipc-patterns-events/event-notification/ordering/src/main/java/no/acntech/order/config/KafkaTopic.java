package no.acntech.order.config;

import java.util.Collections;
import java.util.List;

public enum KafkaTopic {

    ORDERS("event-notification-orders"),
    RESERVATIONS("event-notification-reservations");

    private String name;

    KafkaTopic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> toList() {
        return Collections.singletonList(name);
    }
}
