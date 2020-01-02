import { IntlState } from 'react-intl-redux';

import { DEFAULT_LOCALE, messages } from '../../core/locales';
import { ConfigState, CustomerState, ItemState, NotificationState, OrderState, ProductState, RootState, SecurityType, UserState } from '../../models';

export const INITIAL_CONFIG_STATE: ConfigState = {
    loading: false,
    config: {
        security: {
            type: SecurityType.NONE
        }
    },
    values: {
        loginLink: '/login'
    }
};

export const INITIAL_INTL_STATE: IntlState = {
    locale: DEFAULT_LOCALE,
    messages: messages[DEFAULT_LOCALE]
};

export const INITIAL_NOTIFICATION_STATE: NotificationState = {
    notifications: []
};

export const INITIAL_USER_STATE: UserState = {};

export const INITIAL_CUSTOMER_STATE: CustomerState = {
    loading: false,
    customers: []
};

export const INITIAL_PRODUCT_STATE: ProductState = {
    loading: false,
    products: []
};

export const INITIAL_ORDER_STATE: OrderState = {
    loading: false,
    orders: []
};

export const INITIAL_ITEM_STATE: ItemState = {
    loading: false,
    items: []
};

export const INITIAL_ROOT_STATE: RootState = {
    configState: INITIAL_CONFIG_STATE,
    intl: INITIAL_INTL_STATE,
    notificationState: INITIAL_NOTIFICATION_STATE,
    userState: INITIAL_USER_STATE,
    customerState: INITIAL_CUSTOMER_STATE,
    productState: INITIAL_PRODUCT_STATE,
    orderState: INITIAL_ORDER_STATE,
    itemState: INITIAL_ITEM_STATE
};