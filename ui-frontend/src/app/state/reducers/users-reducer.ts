import { HandleUserActionType, LoginUserAction, LogoutUserAction, UserAction, UserState } from '../../models';
import { INITIAL_CUSTOMER_STATE, INITIAL_USER_STATE } from '../store/initial-state';

export const reducer = (state: UserState = INITIAL_USER_STATE, action: UserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGIN:
            return login(state, action);
        case HandleUserActionType.LOGOUT:
            return logout(state, action);
        default:
            return state;
    }
};

const login = (state: UserState = INITIAL_USER_STATE, action: LoginUserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGIN: {
            const {user} = action;
            return {...INITIAL_CUSTOMER_STATE, user: user};
        }

        default: {
            return state;
        }
    }
};

const logout = (state: UserState = INITIAL_USER_STATE, action: LogoutUserAction): UserState => {
    switch (action.type) {
        case HandleUserActionType.LOGOUT: {
            return {...INITIAL_CUSTOMER_STATE, user: undefined};
        }

        default: {
            return state;
        }
    }
};
