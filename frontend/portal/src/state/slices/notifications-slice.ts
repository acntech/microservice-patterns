import {createSlice, PayloadAction} from '@reduxjs/toolkit';
import {v4 as uuid} from 'uuid';

import {AppDispatch, AppThunk} from '../store';
import {ErrorPayload, Notification, NotificationContent, NotificationSeverity, SliceState} from '../../types';
import {createDateTime} from "../../core/utils";

const initialState: SliceState<Notification[], ErrorPayload> = {
    status: 'IDLE',
    data: []
};

/* eslint-disable no-param-reassign */
const notificationsSlice = createSlice({
    name: 'securityContextSlice',
    initialState,
    reducers: {
        actionAdd: (state, action: PayloadAction<Notification>) => {
            const {payload} = action;
            state.data = state.data ? state.data.concat(payload) : [payload];
        },
        actionRemove: (state, action: PayloadAction<string>) => {
            if (state.data?.length) {
                const {payload} = action;
                state.data = state.data.filter(notification => notification.id !== payload);
            }
        }
    }
});

export const notificationsReducer = notificationsSlice.reducer;

const {
    actionAdd,
    actionRemove
} = notificationsSlice.actions;

const createNotification = (severity: NotificationSeverity, title: string, content?: NotificationContent, permanent?: boolean): Notification => {
    return {
        id: uuid(),
        severity,
        title,
        content,
        permanent,
        created: createDateTime()
    }
};

export const addInfoNotification = (title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification('INFO', title, content, permanent)));
};

export const addWarningNotification = (title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification('WARNING', title, content, permanent)));
};

export const addErrorNotification = (title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification('ERROR', title, content, permanent)));
};

export const addSuccessNotification = (title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification('SUCCESS', title, content, permanent)));
};

export const addFailedNotification = (title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification('FAILED', title, content, permanent)));
};

export const addNotification = (severity: NotificationSeverity, title: string, content?: NotificationContent, permanent?: boolean): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionAdd(createNotification(severity, title, content, permanent)));
};

export const removeNotification = (uid: string): AppThunk => async (dispatch: AppDispatch) => {
    dispatch(actionRemove(uid));
};
