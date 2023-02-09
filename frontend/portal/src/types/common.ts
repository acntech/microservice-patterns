import {ErrorPayload} from "./rest-client";

export type PageStatus = 'PENDING' | 'LOADING' | 'SUCCESS' | 'FAILED'

export interface PageResponse<T> {
    status: number;
    body?: T;
}

export interface PageState<T> {
    status: PageStatus;
    data?: PageResponse<T>;
    error?: PageResponse<ErrorPayload>
}
