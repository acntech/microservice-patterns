import {createSlice, PayloadAction} from '@reduxjs/toolkit';

import {RestClient} from '../../core/client';
import {AppDispatch, AppThunk} from '../store';
import {ClientError, ClientResponse, ErrorPayload, Order, SliceState, SliceStatus} from '../../types';

const initialState: SliceState<Order[], ErrorPayload> = {
    status: 'IDLE'
};

const ORDERS_PATH = '/api/orders';

/* eslint-disable no-param-reassign */
const slice = createSlice({
    name: 'orderListSlice',
    initialState,
    reducers: {
        actionLoading: (state, action: PayloadAction<SliceStatus>) => {
            state.status = action.payload;
        },
        actionSuccess: (state, action: PayloadAction<ClientResponse<Order[]>>) => {
            state.data = action.payload?.body;
            state.status = 'SUCCESS';
        },
        actionError: (state, action: PayloadAction<ClientError<ErrorPayload>>) => {
            state.error = action.payload?.response?.body;
            state.status = 'FAILED';
        }
    }
});

export const orderListReducer = slice.reducer;

const {
    actionLoading,
    actionSuccess,
    actionError
} = slice.actions;

export const findOrderList = (): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.GET<Order[]>(ORDERS_PATH);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
}
