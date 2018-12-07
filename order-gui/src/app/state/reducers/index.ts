import { combineReducers } from 'redux';

import { RootState } from '../../models';
import * as notifications from './notifications';
import * as orders from './orders';

const { reducer: notificationsReducer } = notifications;
const { reducer: ordersReducer } = orders;

export const rootReducer = combineReducers<RootState>({
    notificationState: notificationsReducer,
    orderState: ordersReducer
});