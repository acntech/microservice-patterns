import { configureStore, applyMiddleware, Action } from '@reduxjs/toolkit';
import { ThunkAction } from 'redux-thunk';

import { defaultLocale, getIntlState } from '../core/locales';
import rootReducer, { RootState } from './reducer';
import { loginAwareMiddleware } from './middleware';

const devTools = process.env.NODE_ENV !== 'production';

const updatedIntlState = getIntlState(defaultLocale);

const middlewareEnhancer = applyMiddleware(loginAwareMiddleware);
const enhancers = [middlewareEnhancer];
const preloadedState = {
    intl: updatedIntlState
};

const store = configureStore({
    reducer: rootReducer,
    devTools,
    enhancers,
    preloadedState
});

export type AppDispatch = typeof store.dispatch;
export type AppThunk = ThunkAction<void, RootState, null, Action<string>>;

export default store;