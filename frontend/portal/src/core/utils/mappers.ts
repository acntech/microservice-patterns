import {EnrichedOrder, EnrichedOrderItem, Order, OrderItem, OrderItemStatus, OrderStatus, Product} from "../../types";
import {Variant} from "react-bootstrap/types";

export namespace Mapper {

    export const mapOrderStatusLabelColor = (status: OrderStatus): Variant => {
        switch (status) {
            case OrderStatus.PENDING: {
                return 'primary';
            }
            case OrderStatus.CONFIRMED: {
                return 'success';
            }
            case OrderStatus.CANCELED: {
                return 'warning';
            }
            case OrderStatus.REJECTED: {
                return 'danger';
            }
            default: {
                return 'secondary';
            }
        }
    };

    export const mapOrderItemStatusLabelColor = (status: OrderItemStatus): Variant => {
        switch (status) {
            case OrderItemStatus.PENDING: {
                return 'primary';
            }
            case OrderItemStatus.RESERVED: {
                return 'success';
            }
            case OrderItemStatus.CONFIRMED: {
                return 'success';
            }
            case OrderItemStatus.CANCELED: {
                return 'warning';
            }
            case OrderItemStatus.REJECTED: {
                return 'danger';
            }
            case OrderItemStatus.FAILED: {
                return 'danger';
            }
            default: {
                return 'secondary';
            }
        }
    };

    export const mapEnrichOrderItem = (orderItem: OrderItem, products: Product[]): EnrichedOrderItem => {
        const product = products
            .filter(product => product.productId === orderItem.productId)
            .pop();

        return {
            orderId: orderItem.orderId,
            itemId: orderItem.itemId,
            productId: orderItem.productId,
            name: product?.name,
            quantity: orderItem.quantity,
            price: product?.price,
            currency: product?.currency,
            status: orderItem.status,
            statusColor: mapOrderItemStatusLabelColor(orderItem.status),
            created: orderItem.created,
            modified: orderItem.modified
        }
    };

    export const mapEnrichOrder = (order: Order, products: Product[]): EnrichedOrder => {
        const enrichedItems = order.items.map(item => mapEnrichOrderItem(item, products));

        return {
            orderId: order.orderId,
            customerId: order.customerId,
            name: order.name,
            description: order.description,
            status: order.status,
            statusColor: mapOrderStatusLabelColor(order.status),
            items: order.items,
            enrichedItems: enrichedItems,
            created: order.created,
            modified: order.modified
        }
    }
}