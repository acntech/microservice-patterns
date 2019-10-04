import client from '../../core/client';

import {
    FindProductsActionType, FindProductsErrorAction, FindProductsLoadingAction, FindProductsSuccessAction, GetProductActionType, GetProductErrorAction, GetProductLoadingAction,
    GetProductSuccessAction, Product, ProductQuery
} from '../../models';
import { showErrorNotification } from '../actions';

const getProductLoading = (loading: boolean): GetProductLoadingAction => ({type: GetProductActionType.LOADING, loading});
const getProductSuccess = (payload: Product): GetProductSuccessAction => ({type: GetProductActionType.SUCCESS, payload});
const getProductError = (error: any): GetProductErrorAction => ({type: GetProductActionType.ERROR, error});

const findProductsLoading = (loading: boolean): FindProductsLoadingAction => ({type: FindProductsActionType.LOADING, loading});
const findProductsSuccess = (payload: Product[]): FindProductsSuccessAction => ({type: FindProductsActionType.SUCCESS, payload});
const findProductsError = (error: any): FindProductsErrorAction => ({type: FindProductsActionType.ERROR, error});

const rootPath = 'products';

export function getProduct(productId: string) {
    return (dispatch) => {
        dispatch(getProductLoading(true));
        const url = `${rootPath}/${productId}`;
        return client.get(url)
            .then((response) => {
                return dispatch(getProductSuccess(response));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error getting product', message, true));
                return dispatch(getProductError(error));
            });
    };
}

export function findProducts(query?: ProductQuery) {
    return (dispatch) => {
        dispatch(findProductsLoading(true));
        const url = `${rootPath}`;
        return client.get(url, {params: {...query}})
            .then((response) => {
                return dispatch(findProductsSuccess(response));
            })
            .catch((error) => {
                const {message} = error.response && error.response.data;
                dispatch(showErrorNotification('Error finding products', message, true));
                return dispatch(findProductsError(error));
            });
    };
}
