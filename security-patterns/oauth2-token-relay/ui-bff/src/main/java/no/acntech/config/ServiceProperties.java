package no.acntech.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConfigurationProperties(prefix = "acntech.service")
@Valid
public class ServiceProperties {

    @NotNull
    private Service orders = new Service();
    @NotNull
    private Service items = new Service();
    @NotNull
    private Service products = new Service();
    @NotNull
    private Service reservations = new Service();

    public Service getOrders() {
        return orders;
    }

    public void setOrders(Service orders) {
        this.orders = orders;
    }

    public Service getItems() {
        return items;
    }

    public void setItems(Service items) {
        this.items = items;
    }

    public Service getProducts() {
        return products;
    }

    public void setProducts(Service products) {
        this.products = products;
    }

    public Service getReservations() {
        return reservations;
    }

    public void setReservations(Service reservations) {
        this.reservations = reservations;
    }

    @Valid
    public static class Service {

        @NotNull
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
