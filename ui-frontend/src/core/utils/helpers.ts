import {OrderItemStatus, OrderStatus} from "../../types";
import {SemanticCOLORS} from "semantic-ui-react";

export const getOrderStatusLabelColor = (status: OrderStatus): SemanticCOLORS => {
    switch (status) {
        case OrderStatus.PENDING: {
            return 'blue';
        }
        case OrderStatus.CONFIRMED: {
            return 'green';
        }
        case OrderStatus.CANCELED: {
            return 'yellow';
        }
        case OrderStatus.REJECTED: {
            return 'red';
        }
        default: {
            return 'grey';
        }
    }
};

export const getOrderItemStatusLabelColor = (status: OrderItemStatus): SemanticCOLORS => {
    switch (status) {
        case OrderItemStatus.PENDING: {
            return 'blue';
        }
        case OrderItemStatus.RESERVED: {
            return 'green';
        }
        case OrderItemStatus.CONFIRMED: {
            return 'green';
        }
        case OrderItemStatus.CANCELED: {
            return 'yellow';
        }
        case OrderItemStatus.REJECTED: {
            return 'red';
        }
        case OrderItemStatus.FAILED: {
            return 'red';
        }
        default: {
            return 'grey';
        }
    }
};
