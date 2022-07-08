import {Currency, Entity, FindProductsActionType, GetProductActionType} from '../';
import {Modified, StateError} from './';

export interface Product extends Entity {
    productId: string;
    name: string;
    description: string;
    stock: number;
    price: number;
    currency: Currency;
}

export interface ProductQuery {
    name: string;
}

export interface ProductState {
    loading: boolean;
    products: Product[];
    error?: StateError;
    modified?: Modified;
}

export interface GetProductLoadingAction {
    type: GetProductActionType.LOADING,
    loading: boolean
}

export interface GetProductSuccessAction {
    type: GetProductActionType.SUCCESS,
    payload: Product
}

export interface GetProductErrorAction {
    type: GetProductActionType.ERROR,
    error: any
}

export interface FindProductsLoadingAction {
    type: FindProductsActionType.LOADING,
    loading: boolean
}

export interface FindProductsSuccessAction {
    type: FindProductsActionType.SUCCESS,
    payload: Product[]
}

export interface FindProductsErrorAction {
    type: FindProductsActionType.ERROR,
    error: any
}

export type GetProductAction = GetProductLoadingAction | GetProductSuccessAction | GetProductErrorAction;
export type FindProductsAction = FindProductsLoadingAction | FindProductsSuccessAction | FindProductsErrorAction;

export type ProductAction = GetProductAction | FindProductsAction;
