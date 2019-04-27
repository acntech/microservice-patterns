import { Currency, FindProductsActionType } from '../';
import { Error, Modified } from './';

export interface Product {
    productId: string;
    name: string;
    description: string;
    stock: number;
    price: number;
    currency: Currency;
    created: string;
    modified: string;
}

export interface ProductQuery {
    name: string;
}

export interface ProductState {
    loading: boolean;
    products: Product[];
    error?: Error;
    modified?: Modified;
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

export type FindProductsAction = FindProductsLoadingAction | FindProductsSuccessAction | FindProductsErrorAction;

export type ProductAction = FindProductsAction;
