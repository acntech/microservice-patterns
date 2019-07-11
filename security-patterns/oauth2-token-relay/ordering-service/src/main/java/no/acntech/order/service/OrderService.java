package no.acntech.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import no.acntech.order.model.CreateItemDto;
import no.acntech.order.model.CreateOrderDto;
import no.acntech.order.model.DeleteItemDto;
import no.acntech.order.model.OrderDto;
import no.acntech.order.model.OrderQuery;
import no.acntech.order.model.UpdateItemDto;

@Service
public class OrderService {

    public List<OrderDto> findOrders(OrderQuery orderQuery) {
        return null;
    }

    public OrderDto getOrder(UUID orderId) {
        return null;
    }

    public OrderDto createOrder(CreateOrderDto createOrder) {
        return null;
    }

    public OrderDto updateOrder(UUID orderId) {
        return null;
    }

    public OrderDto deleteOrder(UUID orderId) {
        return null;
    }

    public OrderDto createItem(UUID orderId, CreateItemDto createItem) {
        return null;
    }

    public OrderDto updateItem(UUID orderId, UpdateItemDto updateItem) {
        return null;
    }

    public OrderDto deleteItem(UUID orderId, DeleteItemDto deleteItemDto) {
        return null;
    }
}
