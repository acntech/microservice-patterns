import { CustomerState, NotificationState, OrderState, RootState } from '../../models';

export const initialNotificationState: NotificationState = {
    notifications: []
};

export const initialCustomerState: CustomerState = {
    loading: false,
    customers: []
};

export const initialOrderState: OrderState = {
    loading: false,
    orders: []
};

export const initialRootState: RootState = {
    notificationState: initialNotificationState,
    customerState: initialCustomerState,
    orderState: initialOrderState
};