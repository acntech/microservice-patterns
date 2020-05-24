export enum AuthenticationType {
    NONE = 'NONE',
    FORM_LOGIN = 'FORM_LOGIN',
    OAUTH2_CLIENT = 'OAUTH2_CLIENT'
}

export enum AuthenticationActionType {
    LOGIN = '[authentication] LOGIN',
    LOGOUT = '[authentication] LOGOUT'
}
