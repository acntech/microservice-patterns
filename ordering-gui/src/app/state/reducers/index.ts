import { combineReducers } from 'redux';

import { RootState } from '../../models';
import * as customers from './customers';
import * as items from './items';
import * as notifications from './notifications';
import * as orders from './orders';

const {reducer: notificationsReducer} = notifications;
const {reducer: customersReducer} = customers;
const {reducer: ordersReducer} = orders;
const {reducer: itemsReducer} = items;

export const rootReducer = combineReducers<RootState>({
    notificationState: notificationsReducer,
    customerState: customersReducer,
    orderState: ordersReducer,
    itemState: itemsReducer
});