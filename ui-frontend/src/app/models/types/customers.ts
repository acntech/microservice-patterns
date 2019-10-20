import { Entity, StateError, FindCustomersActionType, GetCustomerActionType, Modified } from '../';

export interface Customer extends Entity {
    customerId: string;
    firstName: string;
    lastName: string;
    address: string;
}

export interface CustomerQuery {
    firstName: string;
    lastName: string;
}

export interface CustomerState {
    loading: boolean;
    customers: Customer[];
    error?: StateError;
    modified?: Modified;
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

export type CustomerAction = GetCustomerAction | FindCustomersAction;
