import {Cart, CartItem, Order, OrderItem, OrderItemStatus, OrderStatus, Product} from "../../types";
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

    export const mapCartItem = (item: OrderItem, product: Product): CartItem => {
        return {
            itemId: item.itemId,
            productId: product.productId,
            code: product.code,
            status: item.status,
            quantity: item.quantity,
            itemPrice: product.price,
            totalPrice: (item.quantity * product.price),
            currency: product.currency
        }
    };

    export const mapCart = (order: Order, products: Product[]): Cart => {
        const {items} = order;
        const cartItems = items
            .map((item) => {
                const product = products.find((p) => p.productId === item.productId);
                return !!product ? Mapper.mapCartItem(item, product) : undefined;
            })
            .filter((c): c is CartItem => !!c);
        return {
            orderId: order.orderId,
            status: order.status,
            items: cartItems,
            created: order.created,
            modified: order.modified
        }
    };
}