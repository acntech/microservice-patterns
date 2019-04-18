import {
    OrderState,
    NotificationState,
    RootState
} from '../../models';

export const initialNotificationState: NotificationState = {
    notifications: []
};

export const initialOrderState: OrderState = {
    loading: false,
    orders: []
};

export const initialRootState: RootState = {
    notificationState: initialNotificationState,
    orderState: initialOrderState
};