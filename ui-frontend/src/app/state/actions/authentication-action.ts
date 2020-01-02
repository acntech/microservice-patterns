import { AuthenticationActionType, AuthenticationLoginAction, AuthenticationLogoutAction, User } from '../../models';

const loginUserAction = (user: User): AuthenticationLoginAction => ({type: AuthenticationActionType.LOGIN, user});
const logoutUserAction = (): AuthenticationLogoutAction => ({type: AuthenticationActionType.LOGOUT});

export function loginUser(user: User) {
    return (dispatch) => {
        return dispatch(loginUserAction(user));
    };
}

export function logoutUser() {
    return (dispatch) => {
        dispatch(logoutUserAction());
    };
}
