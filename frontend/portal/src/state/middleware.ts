import { AnyAction, Dispatch } from '@reduxjs/toolkit';

export const loginAwareMiddleware = () =>
    (next: Dispatch) =>
        (action: AnyAction) => {
            if (action.payload?.type === 'REDIRECT') {
                window.location.pathname = '/oauth2/authorization/keycloak';
                window.location.hash = '';
            }
            return next(action);
        };