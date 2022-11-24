import { SliceStatus } from "./common";
import { ErrorPayload } from "./rest-client";

export interface UserContext {
    userUid: string;
    username: string;
    firstName: string;
    lastName: string;
    roles: []
}

export interface UserContextState {
    status: SliceStatus;
    userContext?: UserContext;
    error?: ErrorPayload;
}

export interface SecurityContext {
    sessionUid: string;
    userContext: UserContext;
}
