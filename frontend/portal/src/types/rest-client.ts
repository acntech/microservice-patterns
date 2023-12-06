export type ClientMethod =
    | 'GET'
    | 'DELETE'
    | 'HEAD'
    | 'OPTIONS'
    | 'POST'
    | 'PUT'
    | 'PATCH';

export interface ClientRequestConfig {
    url?: string;
    method?: ClientMethod;
    headers?: HeadersInit;
    params?: any;
    body?: any;
}

type ClientHeaders = { [key: string]: string };

export interface ClientResponse<T = any> {
    body?: T;
    status: number;
    headers: ClientHeaders;
}

export class ClientError extends Error {
    readonly response: ClientResponse<ErrorPayload>;

    constructor(response: ClientResponse<ErrorPayload>) {
        super("Client Error");
        this.name = "ClientError";
        this.response = response;
    }
}

export interface Pageable {
    offset: number,
    pageNumber: number,
    pageSize: number,
    paged: boolean,
    unpaged: boolean
}

export interface Sortable {
    sorted: boolean,
    unsorted: boolean,
    empty: boolean
}

export interface Page<T> {
    content: T[],
    totalPages: number,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    totalElements: number,
    pageable: Pageable,
    sort: Sortable
}

export interface ErrorPayload {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}