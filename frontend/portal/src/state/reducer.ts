import {intlReducer} from 'react-intl-redux';
import {combineReducers} from '@reduxjs/toolkit';

import {
    customerListReducer,
    customerReducer,
    notificationsReducer,
    orderListReducer,
    orderReducer,
    productListReducer,
    productReducer,
    securityContextReducer
} from './slices';

const rootReducer = combineReducers({
    intl: intlReducer,
    notificationList: notificationsReducer,
    securityContext: securityContextReducer,
    customer: customerReducer,
    customerList: customerListReducer,
    order: orderReducer,
    orderList: orderListReducer,
    product: productReducer,
    productList: productListReducer
});

export type RootState = ReturnType<typeof rootReducer>;

export default rootReducer;
