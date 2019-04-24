import { SemanticCOLORS } from 'semantic-ui-react';
import { ItemStatus, OrderStatus } from '../../models';

export function formatBytes(bytes, decimals?) {
    if (!bytes) {
        return undefined;
    } else if (bytes === 0) {
        return '0 Bytes';
    } else {
        const k = 1024;
        const dm = decimals <= 0 ? 0 : decimals || 2;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    }
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

export const getItemStatusLabelColor = (status: ItemStatus): SemanticCOLORS => {
    switch (status) {
        case ItemStatus.PENDING: {
            return 'blue';
        }
        case ItemStatus.CONFIRMED: {
            return 'green';
        }
        case ItemStatus.CANCELED: {
            return 'yellow';
        }
        case ItemStatus.REJECTED: {
            return 'red';
        }
        default: {
            return 'grey';
        }
    }
};
