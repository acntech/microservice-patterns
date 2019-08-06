import { HandleUserActionType, LoginUserAction, LogoutUserAction, UserAction, UserState } from '../../models';
import { initialCustomerState, initialUserState } from '../store/initial-state';

export const reducer = (state: UserState = initialUserState, action: UserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGIN:
            return login(state, action);
        case HandleUserActionType.LOGOUT:
            return logout(state, action);
        default:
            return state;
    }
};

const login = (state: UserState = initialUserState, action: LoginUserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGIN: {
            const {user} = action;
            return {...initialCustomerState, user: user};
        }

        default: {
            return state;
        }
    }
};

const logout = (state: UserState = initialUserState, action: LogoutUserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGOUT: {
            return {...initialCustomerState, user: undefined};
        }

        default: {
            return state;
        }
    }
};
