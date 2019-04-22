import { Error, FindCustomersActionType, GetCustomerActionType, LoginCustomerActionType, Modified } from '../';

export interface Customer {
    customerId: string;
    firstName: string;
    lastName: string;
    address: string;
    created: string;
    modified: string;
}

export interface CustomerQuery {
    firstName: string;
    lastName: string;
}

export interface CustomerState {
    loading: boolean;
    user?: Customer;
    customers: Customer[];
    error?: Error;
    modified?: Modified;
}

export interface LoginCustomerAction {
    type: LoginCustomerActionType.LOGIN,
    user: Customer
}

export interface LogoutCustomerAction {
    type: LoginCustomerActionType.LOGOUT
}

export interface GetCustomerLoadingAction {
    type: GetCustomerActionType.LOADING,
    loading: boolean
}

export interface GetCustomerSuccessAction {
    type: GetCustomerActionType.SUCCESS,
    payload: Customer
}

export interface GetCustomerErrorAction {
    type: GetCustomerActionType.ERROR,
    error: any
}

export interface FindCustomersLoadingAction {
    type: FindCustomersActionType.LOADING,
    loading: boolean
}

export interface FindCustomersSuccessAction {
    type: FindCustomersActionType.SUCCESS,
    payload: Customer[]
}

export interface FindCustomersErrorAction {
    type: FindCustomersActionType.ERROR,
    error: any
}

export type GetCustomerAction = GetCustomerLoadingAction | GetCustomerSuccessAction | GetCustomerErrorAction;
export type FindCustomersAction = FindCustomersLoadingAction | FindCustomersSuccessAction | FindCustomersErrorAction;

export type CustomerAction = GetCustomerAction | FindCustomersAction | LoginCustomerAction | LogoutCustomerAction;
