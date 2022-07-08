import {HandleUserActionType} from '../';

export interface User {
    userId: string;
    firstName: string;
    lastName: string;
}

export interface UserState {
    user?: User;
}

export interface LoginUserAction {
    type: HandleUserActionType.LOGIN,
    user: User
}

export interface LogoutUserAction {
    type: HandleUserActionType.LOGOUT
}

export type UserAction = LoginUserAction | LogoutUserAction;
