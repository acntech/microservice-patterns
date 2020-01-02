import { intlReducer } from 'react-intl-redux';
import { combineReducers } from 'redux';

import { RootState } from '../../models';
import * as authentication from './authentication-reducer';
import * as config from './config-reducer';
import * as customers from './customers-reducer';
import * as items from './items-reducer';
import * as notifications from './notifications-reducer';
import * as orders from './orders-reducer';
import * as products from './products-reducer';

const {reducer: configReducer} = config;
const {reducer: notificationsReducer} = notifications;
const {reducer: authenticationReducer} = authentication;
const {reducer: customersReducer} = customers;
const {reducer: productsReducer} = products;
const {reducer: ordersReducer} = orders;
const {reducer: itemsReducer} = items;

export const rootReducer = combineReducers<RootState>({
    intl: intlReducer,
    configState: configReducer,
    notificationState: notificationsReducer,
    authenticationState: authenticationReducer,
    customerState: customersReducer,
    productState: productsReducer,
    orderState: ordersReducer,
    itemState: itemsReducer
});