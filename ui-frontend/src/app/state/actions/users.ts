import {HandleUserActionType, LoginUserAction, LogoutUserAction, User} from '../../models';

const loginUserAction = (user: User): LoginUserAction => ({type: HandleUserActionType.LOGIN, user});
const logoutUserAction = (): LogoutUserAction => ({type: HandleUserActionType.LOGOUT});

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
