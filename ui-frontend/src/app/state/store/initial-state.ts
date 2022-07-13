import {IntlState} from 'react-intl-redux';

import {defaultLocale, messages} from '../../core/locales';
import {
    ConfigState,
    CustomerState,
    ItemState,
    NotificationState,
    OrderState,
    ProductState,
    RootState,
    SecurityType,
    UserState
} from '../../models';

export const initialConfigState: ConfigState = {
    securityType: SecurityType.NONE
};

export const initialIntlState: IntlState = {
    locale: defaultLocale,
    messages: messages[defaultLocale]
};

export const initialNotificationState: NotificationState = {
    notifications: []
};

export const initialUserState: UserState = {};

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
    configState: initialConfigState,
    intl: initialIntlState,
    notificationState: initialNotificationState,
    userState: initialUserState,
    customerState: initialCustomerState,
    productState: initialProductState,
    orderState: initialOrderState,
    itemState: initialItemState
};