import client from '../../core/client';

import {
    CreateOrder, CreateOrderActionType, CreateOrderErrorAction, CreateOrderLoadingAction, CreateOrderSuccessAction, DeleteOrderActionType, DeleteOrderErrorAction, DeleteOrderLoadingAction,
    DeleteOrderSuccessAction, FindOrdersActionType, FindOrdersErrorAction, FindOrdersLoadingAction, FindOrdersSuccessAction, GetOrderActionType, GetOrderErrorAction, GetOrderLoadingAction,
    GetOrderSuccessAction, Order, UpdateOrderActionType, UpdateOrderErrorAction, UpdateOrderLoadingAction, UpdateOrderSuccessAction
} from '../../models';
import { ErrorHandler } from '../../providers/error-handler';
import { showNotification, showSuccessNotification } from '../actions';

const getOrderLoading = (): GetOrderLoadingAction => ({type: GetOrderActionType.LOADING, loading: true});
const getOrderSuccess = (payload: Order): GetOrderSuccessAction => ({type: GetOrderActionType.SUCCESS, payload});
const getOrderError = (error: any): GetOrderErrorAction => ({type: GetOrderActionType.ERROR, error});

const findOrdersLoading = (): FindOrdersLoadingAction => ({type: FindOrdersActionType.LOADING, loading: true});
const findOrdersSuccess = (payload: Order[]): FindOrdersSuccessAction => ({type: FindOrdersActionType.SUCCESS, payload});
const findOrdersError = (error: any): FindOrdersErrorAction => ({type: FindOrdersActionType.ERROR, error});

const createOrderLoading = (): CreateOrderLoadingAction => ({type: CreateOrderActionType.LOADING, loading: true});
const createOrderSuccess = (orderId?: string): CreateOrderSuccessAction => ({type: CreateOrderActionType.SUCCESS, orderId});
const createOrderError = (error: any): CreateOrderErrorAction => ({type: CreateOrderActionType.ERROR, error});

const updateOrderLoading = (): UpdateOrderLoadingAction => ({type: UpdateOrderActionType.LOADING, loading: true});
const updateOrderSuccess = (orderId: string): UpdateOrderSuccessAction => ({type: UpdateOrderActionType.SUCCESS, orderId});
const updateOrderError = (error: any): UpdateOrderErrorAction => ({type: UpdateOrderActionType.ERROR, error});

const deleteOrderLoading = (): DeleteOrderLoadingAction => ({type: DeleteOrderActionType.LOADING, loading: true});
const deleteOrderSuccess = (orderId: string): DeleteOrderSuccessAction => ({type: DeleteOrderActionType.SUCCESS, orderId});
const deleteOrderError = (error: any): DeleteOrderErrorAction => ({type: DeleteOrderActionType.ERROR, error});

const rootPath = 'orders';

export function getOrder(orderId: string) {
    return (dispatch) => {
        dispatch(getOrderLoading());
        const url = `${rootPath}/${orderId}`;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(getOrderSuccess(body));
            })
            .catch((error) => {
                const message = ErrorHandler.handleError({id: 'action.get-order-error.title.text'}, undefined, error);
                dispatch(showNotification(message.severity, message.title, message.content, true));
                return dispatch(getOrderError(error));
            });
    };
}

export function findOrders(orderName?: string) {
    return (dispatch) => {
        dispatch(findOrdersLoading());
        const url = orderName ? `${rootPath}?name=${orderName}` : rootPath;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(findOrdersSuccess(body));
            })
            .catch((error) => {
                const message = ErrorHandler.handleError({id: 'action.find-orders-error.title.text'}, undefined, error);
                dispatch(showNotification(message.severity, message.title, message.content, true));
                return dispatch(findOrdersError(error));
            });
    };
}

export function createOrder(order: CreateOrder) {
    return (dispatch) => {
        dispatch(createOrderLoading());
        const url = `${rootPath}`;
        return client.post(url, order)
            .then((response) => {
                const {entityId} = response;
                dispatch(showSuccessNotification({id: 'action.create-order-success.title.text'}));
                return dispatch(createOrderSuccess(entityId));
            })
            .catch((error) => {
                const message = ErrorHandler.handleError({id: 'action.create-order-error.title.text'}, undefined, error);
                dispatch(showNotification(message.severity, message.title, message.content, true));
                return dispatch(createOrderError(error));
            });
    };
}

export function updateOrder(orderId: string) {
    return (dispatch) => {
        dispatch(updateOrderLoading());
        const url = `${rootPath}/${orderId}`;
        return client.put(url)
            .then(() => {
                dispatch(showSuccessNotification({id: 'action.update-order-success.title.text'}));
                return dispatch(updateOrderSuccess(orderId));
            })
            .catch((error) => {
                const message = ErrorHandler.handleError({id: 'action.update-order-error.title.text'}, undefined, error);
                dispatch(showNotification(message.severity, message.title, message.content, true));
                return dispatch(updateOrderError(error));
            });
    };
}

export function deleteOrder(orderId: string) {
    return (dispatch) => {
        dispatch(deleteOrderLoading());
        const url = `${rootPath}/${orderId}`;
        return client.delete(url)
            .then(() => {
                dispatch(showSuccessNotification({id: 'action.delete-order-success.title.text'}));
                return dispatch(deleteOrderSuccess(orderId));
            })
            .catch((error) => {
                const message = ErrorHandler.handleError({id: 'action.delete-order-error.title.text'}, undefined, error);
                dispatch(showNotification(message.severity, message.title, message.content, true));
                return dispatch(deleteOrderError(error));
            });
    };
}
