import {Action, Order, Product, SessionContext, State} from "../types";

const genericReducer = <T>(state: State<T>, action: Action<T>): State<T> => {
    const {status, data, error} = action;
    switch (status) {
        case 'SUCCESS':
            return {
                status,
                data: data?.body
            }
        case 'FAILED':
            return {
                status,
                error: error?.body
            }
        default:
            return state
    }
};

export const sessionReducer = (state: State<SessionContext>, action: Action<SessionContext>): State<SessionContext> => genericReducer<SessionContext>(state, action);
export const orderReducer = (state: State<Order>, action: Action<Order>): State<Order> => genericReducer<Order>(state, action);
export const orderListReducer = (state: State<Order[]>, action: Action<Order[]>): State<Order[]> => genericReducer<Order[]>(state, action);
export const productReducer = (state: State<Product>, action: Action<Product>): State<Product> => genericReducer<Product>(state, action);
export const productListReducer = (state: State<Product[]>, action: Action<Product[]>): State<Product[]> => genericReducer<Product[]>(state, action);
