import {combineReducers, configureStore, Middleware} from '@reduxjs/toolkit';
import {orderReducer} from "./order-slice";
import {orderListReducer} from "./order-list-slice";
import {productListReducer} from "./product-list-slice";
import {TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import {sessionReducer} from "./session-slice";
import {ClientResponse, ErrorPayload} from "../types";

const rootReducer = combineReducers({
    session: sessionReducer,
    order: orderReducer,
    orders: orderListReducer,
    products: productListReducer
});

const redirectAwareMiddleware: Middleware = storeApi => next => action => {
    if (typeof action === 'function') {
        return action(storeApi.dispatch, storeApi.getState());
    } else {
        const {meta, payload} = action;
        if (!!meta && !!meta.rejectedWithValue && !!payload) {
            const response = payload as ClientResponse<ErrorPayload>;
            const {headers} = response;
            const redirectUrl = headers["location"];
            location.replace(redirectUrl);
        }
        return next(action);
    }
};

const store = configureStore({
    reducer: rootReducer,
    middleware: getDefaultMiddleware => getDefaultMiddleware()
        .prepend(redirectAwareMiddleware)
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

export const useAppDispatch: () => AppDispatch = useDispatch;

export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

export default store;