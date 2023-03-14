import {ClientResponse, ErrorPayload} from "./rest-client";

export type Status = 'PENDING' | 'LOADING' | 'SUCCESS' | 'FAILED'

export interface Action<T> {
    status: Status,
    data?: ClientResponse<T>,
    error?: ClientResponse<ErrorPayload>
}

export interface State<T> {
    status: Status;
    data?: T;
    error?: ErrorPayload
}