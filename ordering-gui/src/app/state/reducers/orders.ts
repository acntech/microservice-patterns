import {
    ActionType,
    CreateOrderAction,
    CreateOrderActionType,
    EntityType,
    FindOrdersAction,
    FindOrdersActionType,
    GetOrderAction,
    GetOrderActionType,
    Order,
    OrderAction,
    OrderState
} from '../../models';
import { initialOrderState } from '../store/initial-state';

export const reducer = (state: OrderState = initialOrderState, action: OrderAction): OrderState => {
    switch (action.type) {
        case CreateOrderActionType.LOADING:
        case CreateOrderActionType.SUCCESS:
        case CreateOrderActionType.ERROR:
            return create(state, action);
        case GetOrderActionType.LOADING:
        case GetOrderActionType.SUCCESS:
        case GetOrderActionType.ERROR:
            return get(state, action);
        case FindOrdersActionType.LOADING:
        case FindOrdersActionType.SUCCESS:
        case FindOrdersActionType.ERROR:
            return find(state, action);
        default:
            return state;
    }
};

export const create = (state: OrderState = initialOrderState, action: CreateOrderAction): OrderState => {
    switch (action.type) {
        case CreateOrderActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders: orders, loading: loading};
        }

        case CreateOrderActionType.SUCCESS: {
            const {orders} = state;
            const {headers} = action;
            let modified;

            if (headers) {
                const { location } = headers;
                const orderId = location.split('orders/')[1];
                modified = {id: orderId, entityType: EntityType.ORDERS, actionType: ActionType.CREATE};
            }

            return {...initialOrderState, orders: orders, modified: modified};
        }

        case CreateOrderActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.CREATE};
            return {...initialOrderState, orders: orders, error: error};
        }

        default: {
            return state;
        }
    }
};

export const get = (state: OrderState = initialOrderState, action: GetOrderAction): OrderState => {
    switch (action.type) {
        case GetOrderActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders: orders, loading: loading};
        }

        case GetOrderActionType.SUCCESS: {
            let {orders} = state;
            const {payload} = action;

            if (payload) {
                orders = replaceOrAppend(orders, payload);
            }

            return {...initialOrderState, orders: orders};
        }

        case GetOrderActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.GET};
            return {...initialOrderState, orders: orders, error: error};
        }

        default: {
            return state;
        }
    }
};

export const find = (state: OrderState = initialOrderState, action: FindOrdersAction): OrderState => {
    switch (action.type) {
        case FindOrdersActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders: orders, loading: loading};
        }

        case FindOrdersActionType.SUCCESS: {
            let {orders} = state;
            const {payload} = action;

            if (payload) {
                payload.forEach(order => {
                    orders = replaceOrAppend(orders, order);
                });
            }

            return {...initialOrderState, orders: orders};
        }

        case FindOrdersActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.FIND};
            return {...initialOrderState, orders: orders, error: error};
        }

        default: {
            return state;
        }
    }
};

const replaceOrAppend = (orders: Order[], order: Order) => {
    const index = orders.map(o => o.orderId).indexOf(order.orderId);

    if (index !== -1) {
        orders[index] = order;
    } else {
        orders = orders.concat(order);
    }

    return orders;
};