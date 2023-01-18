export interface UserContext {
    userUid: string;
    username: string;
    firstName: string;
    lastName: string;
    roles: []
}

export interface SecurityContext {
    sessionUid: string;
    userContext: UserContext;
}
