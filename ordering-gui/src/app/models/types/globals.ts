import { ActionType, CustomerState, EntityType, ItemState, NotificationState, OrderState, ProductState } from '../';

export interface RootState {
    notificationState: NotificationState;
    customerState: CustomerState;
    productState: ProductState;
    orderState: OrderState;
    itemState: ItemState;
}

export interface Error {
    entityType: EntityType;
    actionType: ActionType;
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}

export interface Modified {
    id: number;
    entityType: EntityType;
    actionType: ActionType;
}
