import client from '../../core/client';

import {
    CreateItem, CreateItemActionType, CreateItemErrorAction, CreateItemLoadingAction, CreateItemSuccessAction, DeleteItemActionType, DeleteItemErrorAction, DeleteItemLoadingAction,
    DeleteItemSuccessAction, GetItemActionType, GetItemErrorAction, GetItemLoadingAction, GetItemSuccessAction, Item
} from '../../models';
import { showErrorNotification, showSuccessNotification } from '../actions';

const getItemLoading = (): GetItemLoadingAction => ({type: GetItemActionType.LOADING, loading: true});
const getItemSuccess = (payload: Item): GetItemSuccessAction => ({type: GetItemActionType.SUCCESS, payload});
const getItemError = (error: any): GetItemErrorAction => ({type: GetItemActionType.ERROR, error});

const createItemLoading = (): CreateItemLoadingAction => ({type: CreateItemActionType.LOADING, loading: true});
const createItemSuccess = (itemId: string): CreateItemSuccessAction => ({type: CreateItemActionType.SUCCESS, itemId});
const createItemError = (error: any): CreateItemErrorAction => ({type: CreateItemActionType.ERROR, error});

const deleteItemLoading = (): DeleteItemLoadingAction => ({type: DeleteItemActionType.LOADING, loading: true});
const deleteItemSuccess = (itemId: string): DeleteItemSuccessAction => ({type: DeleteItemActionType.SUCCESS, itemId});
const deleteItemError = (error: any): DeleteItemErrorAction => ({type: DeleteItemActionType.ERROR, error});

export function getItem(itemId: string) {
    return (dispatch) => {
        dispatch(getItemLoading());
        const url = `items/${itemId}`;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(getItemSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.get-item-error.title.text'}, message, true));
                return dispatch(getItemError(error));
            });
    };
}

export function createItem(orderId: string, item: CreateItem) {
    return (dispatch) => {
        dispatch(createItemLoading());
        const url = `orders/${orderId}/items`;
        return client.post(url, item)
            .then((response) => {
                const {entityId} = response;
                dispatch(showSuccessNotification({id: 'action.create-item-success.title.text'}));
                return dispatch(createItemSuccess(entityId));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.create-item-error.title.text'}, message, true));
                return dispatch(createItemError(error));
            });
    };
}

export function deleteItem(itemId: string) {
    return (dispatch) => {
        dispatch(deleteItemLoading());
        const url = `items/${itemId}`;
        return client.delete(url)
            .then(() => {
                dispatch(showSuccessNotification({id: 'action.delete-item-success.title.text'}));
                return dispatch(deleteItemSuccess(itemId));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.delete-item-error.title.text'}, message, true));
                return dispatch(deleteItemError(error));
            });
    };
}
