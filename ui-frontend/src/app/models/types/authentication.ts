import { AuthenticationActionType } from '../';

export interface Authentication {
    user?: User;
}

export interface User {
    userId: string;
    firstName: string;
    lastName: string;
}

export interface AuthenticationState {
    authentication: Authentication;
}

export interface AuthenticationLoginAction {
    type: AuthenticationActionType.LOGIN,
    user: User
}

export interface AuthenticationLogoutAction {
    type: AuthenticationActionType.LOGOUT
}

export type AuthenticationAction = AuthenticationLoginAction | AuthenticationLogoutAction;
