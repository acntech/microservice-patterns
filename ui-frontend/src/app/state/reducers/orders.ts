import {
    ActionType, CreateOrderAction, CreateOrderActionType, DeleteOrderAction, DeleteOrderActionType, EntityType, FindOrdersAction, FindOrdersActionType, GetOrderAction, GetOrderActionType, Order,
    OrderAction, OrderState, UpdateOrderAction, UpdateOrderActionType
} from '../../models';
import { initialOrderState } from '../store/initial-state';

export const reducer = (state: OrderState = initialOrderState, action: OrderAction): OrderState => {
    switch (action.type) {
        case GetOrderActionType.LOADING:
        case GetOrderActionType.SUCCESS:
        case GetOrderActionType.ERROR:
            return get(state, action);
        case FindOrdersActionType.LOADING:
        case FindOrdersActionType.SUCCESS:
        case FindOrdersActionType.ERROR:
            return find(state, action);
        case CreateOrderActionType.LOADING:
        case CreateOrderActionType.SUCCESS:
        case CreateOrderActionType.ERROR:
            return create(state, action);
        case UpdateOrderActionType.LOADING:
        case UpdateOrderActionType.SUCCESS:
        case UpdateOrderActionType.ERROR:
            return update(state, action);
        case DeleteOrderActionType.LOADING:
        case DeleteOrderActionType.SUCCESS:
        case DeleteOrderActionType.ERROR:
            return remove(state, action);
        default:
            return state;
    }
};

const get = (state: OrderState = initialOrderState, action: GetOrderAction): OrderState => {
    switch (action.type) {
        case GetOrderActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders, loading};
        }

        case GetOrderActionType.SUCCESS: {
            let {orders} = state;
            const {payload} = action;

            if (payload) {
                orders = replaceOrAppend(orders, payload);
            }

            return {...initialOrderState, orders};
        }

        case GetOrderActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.GET};
            return {...initialOrderState, orders, error};
        }

        default: {
            return state;
        }
    }
};

const find = (state: OrderState = initialOrderState, action: FindOrdersAction): OrderState => {
    switch (action.type) {
        case FindOrdersActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders, loading};
        }

        case FindOrdersActionType.SUCCESS: {
            let {orders} = state;
            const {payload} = action;

            if (payload) {
                payload.forEach(order => {
                    orders = replaceOrAppend(orders, order);
                });
            }

            return {...initialOrderState, orders};
        }

        case FindOrdersActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.FIND};
            return {...initialOrderState, orders, error};
        }

        default: {
            return state;
        }
    }
};

const create = (state: OrderState = initialOrderState, action: CreateOrderAction): OrderState => {
    const {orders} = state;

    switch (action.type) {
        case CreateOrderActionType.LOADING: {
            const {loading} = action;
            return {...initialOrderState, orders, loading};
        }

        case CreateOrderActionType.SUCCESS: {
            const {orderId} = action;

            const modified = {id: orderId || 'order.id.missing', entityType: EntityType.ORDERS, actionType: ActionType.CREATE};

            return {...initialOrderState, orders, modified};
        }

        case CreateOrderActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.CREATE};
            return {...initialOrderState, orders, error};
        }

        default: {
            return state;
        }
    }
};

const update = (state: OrderState = initialOrderState, action: UpdateOrderAction): OrderState => {
    switch (action.type) {
        case UpdateOrderActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders, loading};
        }

        case UpdateOrderActionType.SUCCESS: {
            const {orders} = state;
            const {orderId} = action;
            const modified = {id: orderId, entityType: EntityType.ORDERS, actionType: ActionType.UPDATE};
            return {...initialOrderState, orders, modified};
        }

        case UpdateOrderActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.UPDATE};
            return {...initialOrderState, orders, error};
        }

        default: {
            return state;
        }
    }
};

const remove = (state: OrderState = initialOrderState, action: DeleteOrderAction): OrderState => {
    switch (action.type) {
        case DeleteOrderActionType.LOADING: {
            const {orders} = state;
            const {loading} = action;
            return {...initialOrderState, orders, loading};
        }

        case DeleteOrderActionType.SUCCESS: {
            const {orders} = state;
            const {orderId} = action;
            const modified = {id: orderId, entityType: EntityType.ORDERS, actionType: ActionType.DELETE};
            return {...initialOrderState, orders, modified};
        }

        case DeleteOrderActionType.ERROR: {
            const {orders} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.DELETE};
            return {...initialOrderState, orders, error};
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