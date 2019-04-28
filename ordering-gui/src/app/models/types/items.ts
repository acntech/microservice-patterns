import { CreateItemActionType, Entity, Error, ItemStatus, Modified } from '../';

export interface CreateItem {
    productId: string;
    quantity: number;
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

export type CreateItemAction = CreateItemLoadingAction | CreateItemSuccessAction | CreateItemErrorAction;

export type ItemAction = CreateItemAction;