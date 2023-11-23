import {configureStore} from '@reduxjs/toolkit';
import {orderReducer} from "./order-slice";
import {orderListReducer} from "./order-list-slice";
import {productListReducer} from "./product-list-slice";
import {TypedUseSelectorHook, useDispatch, useSelector} from "react-redux";
import {sessionReducer} from "./session-slice";

const store = configureStore({
    reducer: {
        session: sessionReducer,
        order: orderReducer,
        orders: orderListReducer,
        products: productListReducer
    },
    middleware: getDefaultMiddleware => getDefaultMiddleware({
        serializableCheck: {
            ignoredActionPaths: ["payload.headers"]
        }
    })
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

export const useAppDispatch: () => AppDispatch = useDispatch;

export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

export default store;