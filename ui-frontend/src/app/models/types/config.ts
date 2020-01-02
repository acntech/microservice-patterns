import { ReactNode } from 'react';
import { GetConfigActionType, Modified, SecurityType, StateError } from '../';

export interface Config {
    security: Security;
}

export interface Security {
    type: SecurityType;
}

export interface ConfigState {
    loading: boolean;
    error?: StateError;
    modified?: Modified;
    config: Config;
    values: {[key: string]: ReactNode};
}

export interface GetConfigLoadingAction {
    type: GetConfigActionType.LOADING,
    loading: boolean
}

export interface GetConfigSuccessAction {
    type: GetConfigActionType.SUCCESS,
    payload: Config
}

export interface GetConfigErrorAction {
    type: GetConfigActionType.ERROR,
    error: any
}

export type GetConfigAction = GetConfigLoadingAction | GetConfigSuccessAction | GetConfigErrorAction;

export type ConfigAction = GetConfigAction;
