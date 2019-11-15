package no.acntech.order.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import no.acntech.order.model.Item;
import no.acntech.order.model.ItemDto;
import no.acntech.order.model.Order;
import no.acntech.order.model.OrderDto;

@Component
public class OrderDtoConverter implements Converter<Order, OrderDto> {

    @Override
    public OrderDto convert(@NonNull final Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .name(order.getName())
                .description(order.getDescription())
                .status(order.getStatus())
                .items(convertItems(order))
                .created(order.getCreated())
                .modified(order.getModified())
                .build();
    }

    private List<ItemDto> convertItems(final Order order) {
        return order.getItems().stream()
                .map(item -> convertItem(order, item))
                .collect(Collectors.toList());
    }

    private ItemDto convertItem(final Order order, final Item item) {
        return ItemDto.builder()
                .itemId(item.getItemId())
                .orderId(order.getOrderId())
                .productId(item.getProductId())
                .reservationId(item.getReservationId())
                .quantity(item.getQuantity())
                .status(item.getStatus())
                .created(item.getCreated())
                .modified(item.getModified())
                .build();
    }
}
