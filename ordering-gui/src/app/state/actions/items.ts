import axios from 'axios';

import {
    CreateItem,
    CreateItemActionType,
    CreateItemErrorAction,
    CreateItemLoadingAction,
    CreateItemSuccessAction, DeleteItem,
    DeleteItemActionType,
    DeleteItemErrorAction,
    DeleteItemLoadingAction,
    DeleteItemSuccessAction
} from '../../models';
import { showError, showSuccess } from '../actions';

const createItemLoading = (loading: boolean): CreateItemLoadingAction => ({type: CreateItemActionType.LOADING, loading});
const createItemSuccess = (headers: any): CreateItemSuccessAction => ({type: CreateItemActionType.SUCCESS, headers});
const createItemError = (error: any): CreateItemErrorAction => ({type: CreateItemActionType.ERROR, error});

const deleteItemLoading = (loading: boolean): DeleteItemLoadingAction => ({type: DeleteItemActionType.LOADING, loading});
const deleteItemSuccess = (orderId: string): DeleteItemSuccessAction => ({type: DeleteItemActionType.SUCCESS, orderId});
const deleteItemError = (error: any): DeleteItemErrorAction => ({type: DeleteItemActionType.ERROR, error});

const rootPath = '/api/orders';

export function createItem(orderId: string, item: CreateItem) {
    return (dispatch) => {
        dispatch(createItemLoading(true));
        const url = `${rootPath}/${orderId}/items`;
        return axios.post(url, item)
            .then((response) => {
                dispatch(showSuccess('Item created successfully'));
                return dispatch(createItemSuccess(response.headers));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error creating item', message, true));
                return dispatch(createItemError(error));
            });
    };
}

export function deleteItem(orderId: string, item: DeleteItem) {
    return (dispatch) => {
        dispatch(deleteItemLoading(true));
        const url = `${rootPath}/${orderId}/items`;
        return axios.delete(url, {data: item})
            .then((response) => {
                dispatch(showSuccess('Item deleted successfully'));
                return dispatch(deleteItemSuccess(orderId));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error deleting item', message, true));
                return dispatch(deleteItemError(error));
            });
    };
}
