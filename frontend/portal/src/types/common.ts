import {Order} from "./orders";
import {ErrorPayload} from "./rest-client";

export interface ApiError {
    errorId: string;
    errorCode: string;
}

export type PageStatus = 'PENDING' | 'LOADING' | 'SUCCESS' | 'FAILED'

export interface PageState<T> {
    status: PageStatus;
    data?: T;
    error?: ErrorPayload
}
