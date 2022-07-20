import {createSlice, PayloadAction} from '@reduxjs/toolkit';

import {RestClient} from '../../core/client';
import {AppDispatch, AppThunk} from '../store';
import {ClientError, ClientResponse, Customer, ErrorPayload, SliceState, SliceStatus} from '../../types';

const initialState: SliceState<Customer, ErrorPayload> = {
    status: 'IDLE'
};

const BASE_URL = '/api/customers';

/* eslint-disable no-param-reassign */
const slice = createSlice({
    name: 'customerSlice',
    initialState,
    reducers: {
        actionLoading: (state, action: PayloadAction<SliceStatus>) => {
            state.status = action.payload;
        },
        actionSuccess: (state, action: PayloadAction<ClientResponse<Customer>>) => {
            state.data = action.payload?.body;
            state.status = 'SUCCESS';
        },
        actionError: (state, action: PayloadAction<ClientError<ErrorPayload>>) => {
            state.error = action.payload?.response?.body;
            state.status = 'FAILED';
        }
    }
});

export const customerReducer = slice.reducer;

const {
    actionLoading,
    actionSuccess,
    actionError
} = slice.actions;

export const getCustomer = (id: string): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.GET<Customer>(`${BASE_URL}/${id}`);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
}
