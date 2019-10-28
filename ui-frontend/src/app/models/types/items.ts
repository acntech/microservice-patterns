import { CreateItemActionType, DeleteItemActionType, Entity, GetItemActionType, ItemStatus, Modified, StateError } from '../';

export interface CreateItem {
    productId: string;
    quantity: number;
}

export interface Item extends Entity {
    itemId: string;
    orderId: string;
    productId: string;
    quantity: number;
    status: ItemStatus;
}

export interface ItemState {
    loading: boolean;
    items: Item[];
    error?: StateError;
    modified?: Modified;
}

export interface GetItemLoadingAction {
    type: GetItemActionType.LOADING,
    loading: boolean
}

export interface GetItemSuccessAction {
    type: GetItemActionType.SUCCESS,
    payload: Item
}

export interface GetItemErrorAction {
    type: GetItemActionType.ERROR,
    error: any
}

export interface CreateItemLoadingAction {
    type: CreateItemActionType.LOADING,
    loading: boolean
}

export interface CreateItemSuccessAction {
    type: CreateItemActionType.SUCCESS,
    itemId: string
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
    itemId: string
}

export interface DeleteItemErrorAction {
    type: DeleteItemActionType.ERROR,
    error: any
}

export type GetItemAction = GetItemLoadingAction | GetItemSuccessAction | GetItemErrorAction;
export type CreateItemAction = CreateItemLoadingAction | CreateItemSuccessAction | CreateItemErrorAction;
export type DeleteItemAction = DeleteItemLoadingAction | DeleteItemSuccessAction | DeleteItemErrorAction;

export type ItemAction = GetItemAction | CreateItemAction | DeleteItemAction;
