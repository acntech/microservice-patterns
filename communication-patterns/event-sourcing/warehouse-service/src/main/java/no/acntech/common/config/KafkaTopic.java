package no.acntech.common.config;

public enum KafkaTopic {

    INVENTORY("event-sourcing-inventory");

    private String name;

    KafkaTopic(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
