export interface UserContext {
    uid: string;
    username: string;
    firstName: string;
    lastName: string;
    roles: []
}

export interface SessionContext {
    sid: string;
    userContext: UserContext;
}
