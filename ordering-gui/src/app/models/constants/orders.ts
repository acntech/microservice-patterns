import { SemanticCOLORS } from 'semantic-ui-react';

export enum OrderStatus {
    PENDING = 'PENDING',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED'
}

export enum ItemStatus {
    PENDING = 'PENDING',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED'
}

export const getStatusLabelColor = (status: OrderStatus | ItemStatus): SemanticCOLORS => {
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
