import {OrderItemStatus, OrderStatus} from "../../types";
import {Variant} from "react-bootstrap/types";

export const getOrderStatusLabelColor = (status: OrderStatus): Variant => {
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

export const getOrderItemStatusLabelColor = (status: OrderItemStatus): Variant => {
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
