import client from '../../core/client';

import {
    CreateOrder, CreateOrderActionType, CreateOrderErrorAction, CreateOrderLoadingAction, CreateOrderSuccessAction, DeleteOrderActionType, DeleteOrderErrorAction, DeleteOrderLoadingAction,
    DeleteOrderSuccessAction, FindOrdersActionType, FindOrdersErrorAction, FindOrdersLoadingAction, FindOrdersSuccessAction, GetOrderActionType, GetOrderErrorAction, GetOrderLoadingAction,
    GetOrderSuccessAction, Order, UpdateOrderActionType, UpdateOrderErrorAction, UpdateOrderLoadingAction, UpdateOrderSuccessAction
} from '../../models';
import { showErrorNotification, showSuccessNotification } from '../actions';

const getOrderLoading = (loading: boolean): GetOrderLoadingAction => ({type: GetOrderActionType.LOADING, loading});
const getOrderSuccess = (payload: Order): GetOrderSuccessAction => ({type: GetOrderActionType.SUCCESS, payload});
const getOrderError = (error: any): GetOrderErrorAction => ({type: GetOrderActionType.ERROR, error});

const findOrdersLoading = (loading: boolean): FindOrdersLoadingAction => ({type: FindOrdersActionType.LOADING, loading});
const findOrdersSuccess = (payload: Order[]): FindOrdersSuccessAction => ({type: FindOrdersActionType.SUCCESS, payload});
const findOrdersError = (error: any): FindOrdersErrorAction => ({type: FindOrdersActionType.ERROR, error});

const createOrderLoading = (loading: boolean): CreateOrderLoadingAction => ({type: CreateOrderActionType.LOADING, loading});
const createOrderSuccess = (headers: Headers): CreateOrderSuccessAction => ({type: CreateOrderActionType.SUCCESS, headers});
const createOrderError = (error: any): CreateOrderErrorAction => ({type: CreateOrderActionType.ERROR, error});

const updateOrderLoading = (loading: boolean): UpdateOrderLoadingAction => ({type: UpdateOrderActionType.LOADING, loading});
const updateOrderSuccess = (orderId: string): UpdateOrderSuccessAction => ({type: UpdateOrderActionType.SUCCESS, orderId});
const updateOrderError = (error: any): UpdateOrderErrorAction => ({type: UpdateOrderActionType.ERROR, error});

const deleteOrderLoading = (loading: boolean): DeleteOrderLoadingAction => ({type: DeleteOrderActionType.LOADING, loading});
const deleteOrderSuccess = (orderId: string): DeleteOrderSuccessAction => ({type: DeleteOrderActionType.SUCCESS, orderId});
const deleteOrderError = (error: any): DeleteOrderErrorAction => ({type: DeleteOrderActionType.ERROR, error});

const rootPath = 'orders';

export function getOrder(orderId: string) {
    return (dispatch) => {
        dispatch(getOrderLoading(true));
        const url = `${rootPath}/${orderId}`;
        return client.get(url)
            .then((response) => {
                return dispatch(getOrderSuccess(response));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error getting order', message, true));
                return dispatch(getOrderError(error));
            });
    };
}

export function findOrders(orderName?: string) {
    return (dispatch) => {
        dispatch(findOrdersLoading(true));
        const url = name ? `${rootPath}?name=${orderName}` : rootPath;
        return client.get(url)
            .then((response) => {
                return dispatch(findOrdersSuccess(response));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error finding orders', message, true));
                return dispatch(findOrdersError(error));
            });
    };
}

export function createOrder(order: CreateOrder) {
    return (dispatch) => {
        dispatch(createOrderLoading(true));
        const url = `${rootPath}`;
        return client.post(url, order)
            .then((response) => {
                dispatch(showSuccessNotification('Order created successfully'));
                return dispatch(createOrderSuccess(response.headers));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error creating order', message, true));
                return dispatch(createOrderError(error));
            });
    };
}

export function updateOrder(orderId: string) {
    return (dispatch) => {
        dispatch(updateOrderLoading(true));
        const url = `${rootPath}/${orderId}`;
        return client.put(url)
            .then(() => {
                dispatch(showSuccessNotification('Order updated successfully'));
                return dispatch(updateOrderSuccess(orderId));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error updating order', message, true));
                return dispatch(updateOrderError(error));
            });
    };
}

export function deleteOrder(orderId: string) {
    return (dispatch) => {
        dispatch(deleteOrderLoading(true));
        const url = `${rootPath}/${orderId}`;
        return client.delete(url)
            .then(() => {
                dispatch(showSuccessNotification('Order deleted successfully'));
                return dispatch(deleteOrderSuccess(orderId));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error deleting order', message, true));
                return dispatch(deleteOrderError(error));
            });
    };
}
