
export enum NotificationActionType {
    SHOW = '[notifications] SHOW',
    DISMISS = '[notifications] DISMISS',
    CLEAR = '[notifications] CLEAR'
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