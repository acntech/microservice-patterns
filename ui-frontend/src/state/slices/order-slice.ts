import {createSlice, PayloadAction} from '@reduxjs/toolkit';

import {RestClient} from '../../core/client';
import {AppDispatch, AppThunk} from '../store';
import {
    ClientError,
    ClientResponse,
    CreateOrder,
    CreateOrderItem,
    ErrorPayload,
    Order,
    SliceState,
    SliceStatus
} from '../../types';

const initialState: SliceState<Order, ErrorPayload> = {
    status: 'IDLE'
};

const ORDERS_PATH = '/api/orders';
const ORDER_ITEMS_PATH = '/api/items';

/* eslint-disable no-param-reassign */
const slice = createSlice({
    name: 'orderSlice',
    initialState,
    reducers: {
        actionLoading: (state, action: PayloadAction<SliceStatus>) => {
            state.status = action.payload;
        },
        actionSuccess: (state, action: PayloadAction<ClientResponse<Order>>) => {
            state.data = action.payload?.body;
            state.status = 'SUCCESS';
        },
        actionError: (state, action: PayloadAction<ClientError<ErrorPayload>>) => {
            state.error = action.payload?.response?.body;
            state.status = 'FAILED';
        }
    }
});

export const orderReducer = slice.reducer;

const {
    actionLoading,
    actionSuccess,
    actionError
} = slice.actions;

export const getOrder = (id: string): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.GET<Order>(`${ORDERS_PATH}/${id}`);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};

export const createOrder = (body: CreateOrder): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.POST<Order>(ORDERS_PATH, body);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};

export const updateOrder = (id: string): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.PUT<Order>(`${ORDERS_PATH}/${id}`);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};

export const deleteOrder = (id: string): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.DELETE<Order>(`${ORDERS_PATH}/${id}`);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};

export const createOrderItem = (id: string, body: CreateOrderItem): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.POST<Order>(`${ORDERS_PATH}/${id}/items`, body);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};

export const deleteOrderItem = (id: string): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.DELETE<Order>(`${ORDER_ITEMS_PATH}/${id}`);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};
