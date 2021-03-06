import client from '../../core/client';

import {
    Customer, CustomerQuery, FindCustomersActionType, FindCustomersErrorAction, FindCustomersLoadingAction, FindCustomersSuccessAction, GetCustomerActionType, GetCustomerErrorAction,
    GetCustomerLoadingAction, GetCustomerSuccessAction
} from '../../models';
import { showErrorNotification } from '../actions';

const getCustomerLoading = (loading: boolean): GetCustomerLoadingAction => ({type: GetCustomerActionType.LOADING, loading});
const getCustomerSuccess = (payload: Customer): GetCustomerSuccessAction => ({type: GetCustomerActionType.SUCCESS, payload});
const getCustomerError = (error: any): GetCustomerErrorAction => ({type: GetCustomerActionType.ERROR, error});

const findCustomersLoading = (loading: boolean): FindCustomersLoadingAction => ({type: FindCustomersActionType.LOADING, loading});
const findCustomersSuccess = (payload: Customer[]): FindCustomersSuccessAction => ({type: FindCustomersActionType.SUCCESS, payload});
const findCustomersError = (error: any): FindCustomersErrorAction => ({type: FindCustomersActionType.ERROR, error});

const rootPath = 'customers';

export function getCustomer(customerId: string) {
    return (dispatch) => {
        dispatch(getCustomerLoading(true));
        const url = `${rootPath}/${customerId}`;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(getCustomerSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification('Error finding customers', message, true));
                return dispatch(getCustomerError(error));
            });
    };
}

export function findCustomers(query?: CustomerQuery) {
    return (dispatch) => {
        dispatch(findCustomersLoading(true));
        const url = `${rootPath}`;
        return client.get(url, {params: {...query}})
            .then((response) => {
                const {body} = response;
                return dispatch(findCustomersSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification('Error finding customers', message, true));
                return dispatch(findCustomersError(error));
            });
    };
}
