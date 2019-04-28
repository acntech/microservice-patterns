import axios from 'axios';

import { FindProductsActionType, FindProductsErrorAction, FindProductsLoadingAction, FindProductsSuccessAction, Product, ProductQuery } from '../../models';
import { showError } from '../actions';

const findInventoriesLoading = (loading: boolean): FindProductsLoadingAction => ({type: FindProductsActionType.LOADING, loading});
const findInventoriesSuccess = (payload: Product[]): FindProductsSuccessAction => ({type: FindProductsActionType.SUCCESS, payload});
const findInventoriesError = (error: any): FindProductsErrorAction => ({type: FindProductsActionType.ERROR, error});

const rootPath = '/api/products';

export function findProducts(productQuery?: ProductQuery) {
    return (dispatch) => {
        dispatch(findInventoriesLoading(true));
        const url = `${rootPath}`;
        return axios.get(url, {params: productQuery})
            .then((response) => {
                return dispatch(findInventoriesSuccess(response.data));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error finding products', message, true));
                return dispatch(findInventoriesError(error));
            });
    };
}
