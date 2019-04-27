export enum OrderStatus {
    PENDING = 'PENDING',
    CONFIRMED = 'CONFIRMED',
    CANCELED = 'CANCELED',
    REJECTED = 'REJECTED'
}

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

export enum CreateItemActionType {
    LOADING = '[items] CREATE LOADING',
    SUCCESS = '[items] CREATE SUCCESS',
    ERROR = '[items] CREATE ERROR'
}
