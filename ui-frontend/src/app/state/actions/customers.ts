import client from '../../core/client';

import {
    Customer, CustomerQuery, FindCustomersActionType, FindCustomersErrorAction, FindCustomersLoadingAction, FindCustomersSuccessAction, GetCustomerActionType, GetCustomerErrorAction,
    GetCustomerLoadingAction, GetCustomerSuccessAction, LoginCustomerAction, LoginCustomerActionType, LogoutCustomerAction
} from '../../models';
import { showError } from '../actions';

const loginCustomerAction = (user: Customer): LoginCustomerAction => ({type: LoginCustomerActionType.LOGIN, user});
const logoutCustomerAction = (): LogoutCustomerAction => ({type: LoginCustomerActionType.LOGOUT});

const getCustomerLoading = (loading: boolean): GetCustomerLoadingAction => ({type: GetCustomerActionType.LOADING, loading});
const getCustomerSuccess = (payload: Customer): GetCustomerSuccessAction => ({type: GetCustomerActionType.SUCCESS, payload});
const getCustomerError = (error: any): GetCustomerErrorAction => ({type: GetCustomerActionType.ERROR, error});

const findCustomersLoading = (loading: boolean): FindCustomersLoadingAction => ({type: FindCustomersActionType.LOADING, loading});
const findCustomersSuccess = (payload: Customer[]): FindCustomersSuccessAction => ({type: FindCustomersActionType.SUCCESS, payload});
const findCustomersError = (error: any): FindCustomersErrorAction => ({type: FindCustomersActionType.ERROR, error});

const rootPath = 'customers';

export function loginCustomer(user: Customer) {
    return (dispatch) => {
        dispatch(loginCustomerAction(user));
    };
}

export function logoutCustomer() {
    return (dispatch) => {
        dispatch(logoutCustomerAction());
    };
}

export function getCustomer(customerId: string) {
    return (dispatch) => {
        dispatch(getCustomerLoading(true));
        const url = `${rootPath}/${customerId}`;
        return client.get(url)
            .then((response) => {
                return dispatch(getCustomerSuccess(response.data));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error finding customers', message, true));
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
                return dispatch(findCustomersSuccess(response.data));
            })
            .catch((error) => {
                const {data} = error.response;
                const message = data && data.message;
                dispatch(showError('Error finding customers', message, true));
                return dispatch(findCustomersError(error));
            });
    };
}
