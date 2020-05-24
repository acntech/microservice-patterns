import { ReactNode } from 'react';
import { AuthenticationType, GetConfigActionType, Modified, StateError } from '../';

export interface Config {
    security: SecurityConfig;
}

export interface SecurityConfig {
    apiKey?: string;
    authentication: AuthenticationConfig;
}

export interface AuthenticationConfig {
    type: AuthenticationType;
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
