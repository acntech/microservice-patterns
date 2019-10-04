import client from '../../core/client';

import {
    CreateItem, CreateItemActionType, CreateItemErrorAction, CreateItemLoadingAction, CreateItemSuccessAction, DeleteItemActionType, DeleteItemErrorAction, DeleteItemLoadingAction,
    DeleteItemSuccessAction, GetItemActionType, GetItemErrorAction, GetItemLoadingAction, GetItemSuccessAction, Item
} from '../../models';
import { showErrorNotification, showSuccessNotification } from '../actions';

const getItemLoading = (loading: boolean): GetItemLoadingAction => ({type: GetItemActionType.LOADING, loading});
const getItemSuccess = (payload: Item): GetItemSuccessAction => ({type: GetItemActionType.SUCCESS, payload});
const getItemError = (error: any): GetItemErrorAction => ({type: GetItemActionType.ERROR, error});

const createItemLoading = (loading: boolean): CreateItemLoadingAction => ({type: CreateItemActionType.LOADING, loading});
const createItemSuccess = (headers: Headers): CreateItemSuccessAction => ({type: CreateItemActionType.SUCCESS, headers});
const createItemError = (error: any): CreateItemErrorAction => ({type: CreateItemActionType.ERROR, error});

const deleteItemLoading = (loading: boolean): DeleteItemLoadingAction => ({type: DeleteItemActionType.LOADING, loading});
const deleteItemSuccess = (itemId: string): DeleteItemSuccessAction => ({type: DeleteItemActionType.SUCCESS, itemId});
const deleteItemError = (error: any): DeleteItemErrorAction => ({type: DeleteItemActionType.ERROR, error});

export function getItem(itemId: string) {
    return (dispatch) => {
        dispatch(getItemLoading(true));
        const url = `items/${itemId}`;
        return client.get(url)
            .then((response) => {
                return dispatch(getItemSuccess(response));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error getting item', message, true));
                return dispatch(getItemError(error));
            });
    };
}

export function createItem(orderId: string, item: CreateItem) {
    return (dispatch) => {
        dispatch(createItemLoading(true));
        const url = `orders/${orderId}/items`;
        return client.post(url, item)
            .then((response) => {
                dispatch(showSuccessNotification('Item created successfully'));
                return dispatch(createItemSuccess(response.headers));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error creating item', message, true));
                return dispatch(createItemError(error));
            });
    };
}

export function deleteItem(itemId: string) {
    return (dispatch) => {
        dispatch(deleteItemLoading(true));
        const url = `items/${itemId}`;
        return client.delete(url)
            .then(() => {
                dispatch(showSuccessNotification('Item deleted successfully'));
                return dispatch(deleteItemSuccess(itemId));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error deleting item', message, true));
                return dispatch(deleteItemError(error));
            });
    };
}
