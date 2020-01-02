import { intlReducer } from 'react-intl-redux';
import { combineReducers } from 'redux';

import { RootState } from '../../models';
import * as config from './config-reducer';
import * as customers from './customers-reducer';
import * as items from './items-reducer';
import * as notifications from './notifications-reducer';
import * as orders from './orders-reducer';
import * as products from './products-reducer';
import * as users from './users-reducer';

const {reducer: configReducer} = config;
const {reducer: notificationsReducer} = notifications;
const {reducer: usersReducer} = users;
const {reducer: customersReducer} = customers;
const {reducer: productsReducer} = products;
const {reducer: ordersReducer} = orders;
const {reducer: itemsReducer} = items;

export const rootReducer = combineReducers<RootState>({
    configState: configReducer,
    intl: intlReducer,
    notificationState: notificationsReducer,
    userState: usersReducer,
    customerState: customersReducer,
    productState: productsReducer,
    orderState: ordersReducer,
    itemState: itemsReducer
});