import axios from 'axios';

import {
    FindProductsActionType,
    FindProductsErrorAction,
    FindProductsLoadingAction,
    FindProductsSuccessAction,
    GetProductActionType,
    GetProductErrorAction,
    GetProductLoadingAction,
    GetProductSuccessAction,
    Product,
    ProductQuery
} from '../../models';
import {showError} from '../actions';

const getProductLoading = (loading: boolean): GetProductLoadingAction => ({type: GetProductActionType.LOADING, loading});
const getProductSuccess = (payload: Product): GetProductSuccessAction => ({type: GetProductActionType.SUCCESS, payload});
const getProductError = (error: any): GetProductErrorAction => ({type: GetProductActionType.ERROR, error});

const findProductsLoading = (loading: boolean): FindProductsLoadingAction => ({type: FindProductsActionType.LOADING, loading});
const findProductsSuccess = (payload: Product[]): FindProductsSuccessAction => ({type: FindProductsActionType.SUCCESS, payload});
const findProductsError = (error: any): FindProductsErrorAction => ({type: FindProductsActionType.ERROR, error});

const rootPath = '/api/products';

export function getProduct(productId: string) {
    return (dispatch) => {
        dispatch(getProductLoading(true));
        const url = `${rootPath}/${productId}`;
        return axios.get(url)
            .then((response) => {
                return dispatch(getProductSuccess(response.data));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error getting product', message, true));
                return dispatch(getProductError(error));
            });
    };
}

export function findProducts(productQuery?: ProductQuery) {
    return (dispatch) => {
        dispatch(findProductsLoading(true));
        const url = `${rootPath}`;
        return axios.get(url, {params: productQuery})
            .then((response) => {
                return dispatch(findProductsSuccess(response.data));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error finding products', message, true));
                return dispatch(findProductsError(error));
            });
    };
}
