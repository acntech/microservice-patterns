import {
    CreateOrderActionType,
    DeleteOrderActionType,
    Entity,
    FindOrdersActionType,
    GetOrderActionType,
    Modified,
    OrderStatus,
    StateError,
    UpdateOrderActionType
} from '../';
import {Item} from './';

export interface CreateOrder {
    customerId: string;
    name: string;
    description?: string;
}

export interface Order extends Entity {
    orderId: string;
    customerId: string;
    name: string;
    description?: string;
    status: OrderStatus;
    items: Item[];
}

export interface OrderState {
    loading: boolean;
    orders: Order[];
    error?: StateError;
    modified?: Modified;
}

export interface GetOrderLoadingAction {
    type: GetOrderActionType.LOADING,
    loading: boolean
}

export interface GetOrderSuccessAction {
    type: GetOrderActionType.SUCCESS,
    payload: Order
}

export interface GetOrderErrorAction {
    type: GetOrderActionType.ERROR,
    error: any
}

export interface FindOrdersLoadingAction {
    type: FindOrdersActionType.LOADING,
    loading: boolean
}

export interface FindOrdersSuccessAction {
    type: FindOrdersActionType.SUCCESS,
    payload: Order[]
}

export interface FindOrdersErrorAction {
    type: FindOrdersActionType.ERROR,
    error: any
}

export interface CreateOrderLoadingAction {
    type: CreateOrderActionType.LOADING,
    loading: boolean
}

export interface CreateOrderSuccessAction {
    type: CreateOrderActionType.SUCCESS,
    orderId?: string
}

export interface CreateOrderErrorAction {
    type: CreateOrderActionType.ERROR,
    error: any
}

export interface UpdateOrderLoadingAction {
    type: UpdateOrderActionType.LOADING,
    loading: boolean
}

export interface UpdateOrderSuccessAction {
    type: UpdateOrderActionType.SUCCESS,
    orderId: string
}

export interface UpdateOrderErrorAction {
    type: UpdateOrderActionType.ERROR,
    error: any
}

export interface DeleteOrderLoadingAction {
    type: DeleteOrderActionType.LOADING,
    loading: boolean
}

export interface DeleteOrderSuccessAction {
    type: DeleteOrderActionType.SUCCESS,
    orderId: string
}

export interface DeleteOrderErrorAction {
    type: DeleteOrderActionType.ERROR,
    error: any
}

export type GetOrderAction = GetOrderLoadingAction | GetOrderSuccessAction | GetOrderErrorAction;
export type FindOrdersAction = FindOrdersLoadingAction | FindOrdersSuccessAction | FindOrdersErrorAction;
export type CreateOrderAction = CreateOrderLoadingAction | CreateOrderSuccessAction | CreateOrderErrorAction;
export type UpdateOrderAction = UpdateOrderLoadingAction | UpdateOrderSuccessAction | UpdateOrderErrorAction;
export type DeleteOrderAction = DeleteOrderLoadingAction | DeleteOrderSuccessAction | DeleteOrderErrorAction;

export type OrderAction = GetOrderAction | FindOrdersAction | CreateOrderAction | UpdateOrderAction | DeleteOrderAction;
