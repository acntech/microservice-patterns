import axios from 'axios';

import { CreateItem, CreateItemActionType, CreateItemErrorAction, CreateItemLoadingAction, CreateItemSuccessAction } from '../../models';
import { showError, showSuccess } from '../actions';

const createItemLoading = (loading: boolean): CreateItemLoadingAction => ({type: CreateItemActionType.LOADING, loading});
const createItemSuccess = (headers: any): CreateItemSuccessAction => ({type: CreateItemActionType.SUCCESS, headers});
const createItemError = (error: any): CreateItemErrorAction => ({type: CreateItemActionType.ERROR, error});

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
