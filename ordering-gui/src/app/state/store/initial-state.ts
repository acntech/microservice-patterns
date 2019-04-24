import { CustomerState, ItemState, NotificationState, OrderState, RootState } from '../../models';

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

export const initialItemState: ItemState = {
    loading: false
};

export const initialRootState: RootState = {
    notificationState: initialNotificationState,
    customerState: initialCustomerState,
    orderState: initialOrderState,
    itemState: initialItemState
};