import {CreateItemActionType, DeleteItemActionType, Entity, Error, ItemStatus, Modified} from '../';

export interface CreateItem {
    productId: string;
    quantity: number;
}

export interface DeleteItem {
    productId: string;
}

export interface Item extends Entity {
    orderId: string;
    productId: string;
    quantity: number;
    status: ItemStatus;
}

export interface ItemState {
    loading: boolean;
    error?: Error;
    modified?: Modified;
}

export interface CreateItemLoadingAction {
    type: CreateItemActionType.LOADING,
    loading: boolean
}

export interface CreateItemSuccessAction {
    type: CreateItemActionType.SUCCESS,
    headers: any
}

export interface CreateItemErrorAction {
    type: CreateItemActionType.ERROR,
    error: any
}

export interface DeleteItemLoadingAction {
    type: DeleteItemActionType.LOADING,
    loading: boolean
}

export interface DeleteItemSuccessAction {
    type: DeleteItemActionType.SUCCESS,
    orderId: string
}

export interface DeleteItemErrorAction {
    type: DeleteItemActionType.ERROR,
    error: any
}

export type CreateItemAction = CreateItemLoadingAction | CreateItemSuccessAction | CreateItemErrorAction;
export type DeleteItemAction = DeleteItemLoadingAction | DeleteItemSuccessAction | DeleteItemErrorAction;

export type ItemAction = CreateItemAction | DeleteItemAction;
