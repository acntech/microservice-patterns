import { AuthenticationAction, AuthenticationActionType, AuthenticationLoginAction, AuthenticationLogoutAction, AuthenticationState } from '../../models';
import { INITIAL_AUTHENTICATION_STATE, INITIAL_CUSTOMER_STATE } from '../store/initial-state';

export const reducer = (state: AuthenticationState = INITIAL_AUTHENTICATION_STATE, action: AuthenticationAction): AuthenticationState => {
    switch (action.type) {
        case AuthenticationActionType.LOGIN:
            return login(state, action);
        case AuthenticationActionType.LOGOUT:
            return logout(state, action);
        default:
            return state;
    }
};

const login = (state: AuthenticationState = INITIAL_AUTHENTICATION_STATE, action: AuthenticationLoginAction): AuthenticationState => {
    switch (action.type) {
        case AuthenticationActionType.LOGIN: {
            const {user} = action;
            return {...INITIAL_CUSTOMER_STATE, authentication: {user: user}};
        }

        default: {
            return state;
        }
    }
};

const logout = (state: AuthenticationState = INITIAL_AUTHENTICATION_STATE, action: AuthenticationLogoutAction): AuthenticationState => {
    switch (action.type) {
        case AuthenticationActionType.LOGOUT: {
            return {...INITIAL_CUSTOMER_STATE, authentication: {user: undefined}};
        }

        default: {
            return state;
        }
    }
};
