import { CustomerState, ItemState, NotificationState, OrderState, ProductState, RootState } from '../../models';

export const initialNotificationState: NotificationState = {
    notifications: []
};

export const initialCustomerState: CustomerState = {
    loading: false,
    customers: []
};

export const initialProductState: ProductState = {
    loading: false,
    products: []
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
    productState: initialProductState,
    orderState: initialOrderState,
    itemState: initialItemState
};