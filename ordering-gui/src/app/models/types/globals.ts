import {
    ActionType,
    CustomerState,
    EntityType,
    OrderState,
    NotificationState
} from '../';

export interface RootState {
    notificationState: NotificationState;
    customerState: CustomerState;
    orderState: OrderState;
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
