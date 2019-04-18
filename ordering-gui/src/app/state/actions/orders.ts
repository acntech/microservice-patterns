import axios from 'axios';

import {
    CreateOrder,
    Order,
    CreateOrderActionType,
    CreateOrderErrorAction,
    CreateOrderLoadingAction,
    CreateOrderSuccessAction,
    FindOrdersActionType,
    FindOrdersErrorAction,
    FindOrdersLoadingAction,
    FindOrdersSuccessAction,
    GetOrderActionType,
    GetOrderErrorAction,
    GetOrderLoadingAction,
    GetOrderSuccessAction
} from '../../models';
import { showError, showSuccess } from '../actions';

const getOrderLoading = (loading: boolean): GetOrderLoadingAction => ({ type: GetOrderActionType.LOADING, loading });
const getOrderSuccess = (payload: Order): GetOrderSuccessAction => ({ type: GetOrderActionType.SUCCESS, payload });
const getOrderError = (error: any): GetOrderErrorAction => ({ type: GetOrderActionType.ERROR, error });

const findOrdersLoading = (loading: boolean): FindOrdersLoadingAction => ({ type: FindOrdersActionType.LOADING, loading });
const findOrdersSuccess = (payload: Order[]): FindOrdersSuccessAction => ({ type: FindOrdersActionType.SUCCESS, payload });
const findOrdersError = (error: any): FindOrdersErrorAction => ({ type: FindOrdersActionType.ERROR, error });

const createOrderLoading = (loading: boolean): CreateOrderLoadingAction => ({ type: CreateOrderActionType.LOADING, loading });
const createOrderSuccess = (payload: Order): CreateOrderSuccessAction => ({ type: CreateOrderActionType.SUCCESS, payload });
const createOrderError = (error: any): CreateOrderErrorAction => ({ type: CreateOrderActionType.ERROR, error });

const rootPath = '/api/orders';

export function getOrder(orderId: string) {
    return (dispatch) => {
        dispatch(getOrderLoading(true));
        const url = `${rootPath}/${orderId}`;
        return axios.get(url)
            .then((response) => {
                return dispatch(getOrderSuccess(response.data));
            })
            .catch((error) => {
                return dispatch(getOrderError(error));
            });
    };
}

export function findOrders(orderName?: string) {
    return (dispatch) => {
        dispatch(findOrdersLoading(true));
        const url = name ? `${rootPath}?name=${orderName}` : rootPath;
        return axios.get(url)
            .then((response) => {
                return dispatch(findOrdersSuccess(response.data));
            })
            .catch((error) => {
                const { message } = error.response.data;
                dispatch(showError('Error finding orders', message, true));
                return dispatch(findOrdersError(error));
            });
    };
}

export function createOrder(order: CreateOrder) {
    return (dispatch) => {
        dispatch(createOrderLoading(true));
        const url = `${rootPath}`;
        return axios.post(url, order)
            .then((response) => {
                dispatch(showSuccess('Order created successfully'));
                return dispatch(createOrderSuccess(response.data));
            })
            .catch((error) => {
                const { message } = error.response.data;
                dispatch(showError('Error creating order', message));
                return dispatch(createOrderError(error));
            });
    };
}