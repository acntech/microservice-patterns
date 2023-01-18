import {ErrorPayload} from "./rest-client";

export type PageStatus = 'PENDING' | 'LOADING' | 'SUCCESS' | 'FAILED'

export interface PageState<T> {
    status: PageStatus;
    data?: T;
    error?: ErrorPayload
}
