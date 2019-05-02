import {SemanticCOLORS} from "semantic-ui-react";

export enum ItemStatus {
    PENDING = 'PENDING',
    RESERVED = 'RESERVED',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED',
    FAILED = 'FAILED'
}

export const getItemStatusLabelColor = (status: ItemStatus): SemanticCOLORS => {
    switch (status) {
        case ItemStatus.PENDING: {
            return 'blue';
        }
        case ItemStatus.RESERVED: {
            return 'green';
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
        case ItemStatus.FAILED: {
            return 'red';
        }
        default: {
            return 'grey';
        }
    }
};

export enum CreateItemActionType {
    LOADING = '[items] CREATE LOADING',
    SUCCESS = '[items] CREATE SUCCESS',
    ERROR = '[items] CREATE ERROR'
}

export enum DeleteItemActionType {
    LOADING = '[items] DELETE LOADING',
    SUCCESS = '[items] DELETE SUCCESS',
    ERROR = '[items] DELETE ERROR'
}
