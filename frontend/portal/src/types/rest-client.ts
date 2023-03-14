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

export interface ClientResponse<T = any> {
    body?: T;
    status: number;
    headers: Headers;
}

export interface ClientError<T = any> extends Error {
    response?: ClientResponse<T>;
}

export interface ClientErrorConstructor<T = any> extends ErrorConstructor {
    new(response?: ClientResponse<T>): ClientError<T>;

    (response?: ClientResponse<T>): ClientError<T>;

    readonly prototype: ClientError<T>;
}

export declare let ClientResponseError: ClientErrorConstructor<ErrorPayload>;

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