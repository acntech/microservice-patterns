import { IntlState } from 'react-intl-redux';

import { defaultLocale, messages } from '../../core/locales';
import { CustomerState, ItemState, NotificationState, OrderState, ProductState, RootState } from '../../models';

export const initialIntlState: IntlState = {
    locale: defaultLocale,
    messages: messages[defaultLocale]
};

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
    loading: false,
    items: []
};

export const initialRootState: RootState = {
    intl: initialIntlState,
    notificationState: initialNotificationState,
    customerState: initialCustomerState,
    productState: initialProductState,
    orderState: initialOrderState,
    itemState: initialItemState
};