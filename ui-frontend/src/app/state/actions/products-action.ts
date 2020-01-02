import client from '../../core/client';

import {
    FindProductsActionType, FindProductsErrorAction, FindProductsLoadingAction, FindProductsSuccessAction, GetProductActionType, GetProductErrorAction, GetProductLoadingAction,
    GetProductSuccessAction, Product, ProductQuery
} from '../../models';
import { showErrorNotification } from '../actions';

const getProductLoading = (): GetProductLoadingAction => ({type: GetProductActionType.LOADING, loading: true});
const getProductSuccess = (payload: Product): GetProductSuccessAction => ({type: GetProductActionType.SUCCESS, payload});
const getProductError = (error: any): GetProductErrorAction => ({type: GetProductActionType.ERROR, error});

const findProductsLoading = (): FindProductsLoadingAction => ({type: FindProductsActionType.LOADING, loading: true});
const findProductsSuccess = (payload: Product[]): FindProductsSuccessAction => ({type: FindProductsActionType.SUCCESS, payload});
const findProductsError = (error: any): FindProductsErrorAction => ({type: FindProductsActionType.ERROR, error});

const rootPath = 'products';

export function getProduct(productId: string) {
    return (dispatch) => {
        dispatch(getProductLoading());
        const url = `${rootPath}/${productId}`;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(getProductSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.get-product-error.title.text'}, message, true));
                return dispatch(getProductError(error));
            });
    };
}

export function findProducts(query?: ProductQuery) {
    return (dispatch) => {
        dispatch(findProductsLoading());
        const url = `${rootPath}`;
        return client.get(url, {params: {...query}})
            .then((response) => {
                const {body} = response;
                return dispatch(findProductsSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.find-products-error.title.text'}, message, true));
                return dispatch(findProductsError(error));
            });
    };
}
