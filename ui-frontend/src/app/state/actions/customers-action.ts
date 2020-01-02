import client from '../../core/client';

import {
    Customer, CustomerQuery, FindCustomersActionType, FindCustomersErrorAction, FindCustomersLoadingAction, FindCustomersSuccessAction, GetCustomerActionType, GetCustomerErrorAction,
    GetCustomerLoadingAction, GetCustomerSuccessAction
} from '../../models';
import { showErrorNotification } from '../actions';

const getCustomerLoading = (): GetCustomerLoadingAction => ({type: GetCustomerActionType.LOADING, loading: true});
const getCustomerSuccess = (payload: Customer): GetCustomerSuccessAction => ({type: GetCustomerActionType.SUCCESS, payload});
const getCustomerError = (error: any): GetCustomerErrorAction => ({type: GetCustomerActionType.ERROR, error});

const findCustomersLoading = (): FindCustomersLoadingAction => ({type: FindCustomersActionType.LOADING, loading: true});
const findCustomersSuccess = (payload: Customer[]): FindCustomersSuccessAction => ({type: FindCustomersActionType.SUCCESS, payload});
const findCustomersError = (error: any): FindCustomersErrorAction => ({type: FindCustomersActionType.ERROR, error});

const rootPath = 'customers';

export function getCustomer(customerId: string) {
    return (dispatch) => {
        dispatch(getCustomerLoading());
        const url = `${rootPath}/${customerId}`;
        return client.get(url)
            .then((response) => {
                const {body} = response;
                return dispatch(getCustomerSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.get-customer-error.title.text'}, message, true));
                return dispatch(getCustomerError(error));
            });
    };
}

export function findCustomers(query?: CustomerQuery) {
    return (dispatch) => {
        dispatch(findCustomersLoading());
        const url = `${rootPath}`;
        return client.get(url, {params: {...query}})
            .then((response) => {
                const {body} = response;
                return dispatch(findCustomersSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.find-customers-error.title.text'}, message, true));
                return dispatch(findCustomersError(error));
            });
    };
}
