import { SemanticCOLORS } from 'semantic-ui-react';

export enum OrderStatus {
    PENDING = 'PENDING',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED'
}

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

export enum GetOrderActionType {
    LOADING = '[orders] GET LOADING',
    SUCCESS = '[orders] GET SUCCESS',
    ERROR = '[orders] GET ERROR'
}

export enum FindOrdersActionType {
    LOADING = '[orders] FIND LOADING',
    SUCCESS = '[orders] FIND SUCCESS',
    ERROR = '[orders] FIND ERROR'
}

export enum CreateOrderActionType {
    LOADING = '[orders] CREATE LOADING',
    SUCCESS = '[orders] CREATE SUCCESS',
    ERROR = '[orders] CREATE ERROR'
}

export enum UpdateOrderActionType {
    LOADING = '[orders] UPDATE LOADING',
    SUCCESS = '[orders] UPDATE SUCCESS',
    ERROR = '[orders] UPDATE ERROR'
}

export enum DeleteOrderActionType {
    LOADING = '[orders] DELETE LOADING',
    SUCCESS = '[orders] DELETE SUCCESS',
    ERROR = '[orders] DELETE ERROR'
}
