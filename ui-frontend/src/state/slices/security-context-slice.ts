import {createSlice, PayloadAction} from '@reduxjs/toolkit';

import {RestClient} from '../../core/client';
import {AppDispatch, AppThunk} from '../store';
import {ClientError, ClientResponse, ErrorPayload, SecurityContext, SliceState, SliceStatus} from '../../types';

const initialState: SliceState<SecurityContext, ErrorPayload> = {
    status: 'IDLE'
};

const BASE_URL = "/api/security/context";

/* eslint-disable no-param-reassign */
const securityContextSlice = createSlice({
    name: 'securityContextSlice',
    initialState,
    reducers: {
        actionLoading: (state, action: PayloadAction<SliceStatus>) => {
            state.status = action.payload;
        },
        actionSuccess: (state, action: PayloadAction<ClientResponse<SecurityContext>>) => {
            state.data = action.payload?.body;
            state.status = 'SUCCESS';
        },
        actionError: (state, action: PayloadAction<ClientError<ErrorPayload>>) => {
            state.error = action.payload?.response?.body;
            state.status = 'FAILED';
        }
    }
});

export const securityContextReducer = securityContextSlice.reducer;

const {
    actionLoading,
    actionSuccess,
    actionError
} = securityContextSlice.actions;

export const getSecurityContext = (): AppThunk => async (dispatch: AppDispatch) => {
    try {
        dispatch(actionLoading('LOADING'));
        const response = await RestClient.GET<SecurityContext>(BASE_URL);
        dispatch(actionSuccess(response));
    } catch (e) {
        const error = e as ClientError<ErrorPayload>;
        dispatch(actionError(error));
    }
};
