import { polyfill } from 'es6-promise';
import 'isomorphic-fetch';
import * as _ from 'lodash';
import uuidv4 from 'uuid/v4';

// IE Promise polyfill
polyfill();

export enum RequestMethod {
    GET = 'GET',
    POST = 'POST',
    PUT = 'PUT',
    PATCH = 'PATCH',
    DELETE = 'DELETE'
}

export enum ResponseCode {
    UNKNOWN = 0,
    OK = 200,
    CREATED = 201,
    ACCEPTED = 202,
    NO_CONTENT = 204,
    FOUND = 302,
    BAD_REQUEST = 400,
    UNAUTHORIZED = 401,
    FORBIDDEN = 403,
    NOT_FOUND = 404
}

export enum ResponseType {
    BASIC = 'basic',
    CORS = 'cors',
    DEFAULT = 'default',
    ERROR = 'error',
    OPAQUE = 'opaque',
    OPAQUE_REDIRECT = 'opaqueredirect'
}

export enum HeaderName {
    ACCEPT = 'Accept',
    CONTENT_TYPE = 'Content-Type',
    LOCATION = 'Location',
    X_API_KEY = 'X-Api-Key',
    X_XSRF_TOKEN = 'X-XSRF-TOKEN',
    X_CONVERSATION_ID = 'X-Conversation-Id',
    X_REQUEST_ID = 'X-Request-Id'
}

export enum CookieName {
    XSRF_TOKEN = 'XSRF-TOKEN'
}

export enum ContentType {
    APPLICATION_JSON = 'application/json',
    TEXT_HTML = 'text/html',
    TEXT_PLAIN = 'text/plain',
    NO_BODY = 'no_body',
    UNKNOWN = 'unknown'
}

export interface ClientFields {
    conversationId: string;
    requestId: string;
}

export interface ClientResponse extends ClientFields {
    entityId?: string;
    statusCode: number;
    statusText: string;
    headers: Map<string, string>;
    cookies: Map<string, string>;
    body?: any;
}

export interface ClientErrorResponse extends ClientResponse {
    errorId: string;
    errorCode: string;
    body?: any;
}

export interface ClientError {
    response: ClientErrorResponse;
    error: Error;
}

export interface ClientParam {
    [index: string]: string | number | boolean | any[];
}

export interface ClientRequestConfig {
    headers?: Map<string, string>;
    params?: ClientParam;
    credentialsPolicy?: RequestCredentials;
    redirectPolicy?: RequestRedirect;
}

export enum ClientErrorCode {
    CLIENT_UNKNOWN_ERROR = 'client.unknown_error',
    CLIENT_UNHANDLED_CONTENT_TYPE = 'client.unhandled_content_type',
    CLIENT_UNHANDLED_RESPONSE_TYPE = 'client.unhandled_response_type',
    SERVER_REDIRECT_RESPONSE = 'server.redirect_response'
}

const BASE_URL = '/api';
const DEFAULT_CREDENTIALS_POLICY: RequestCredentials = 'same-origin';
const DEFAULT_REDIRECT_POLICY: RequestRedirect = 'manual';
const UNKNOWN_ERROR_ID = 'unknown_error_id';
const UNKNOWN_ERROR = {errorId: UNKNOWN_ERROR_ID, errorCode: ClientErrorCode.CLIENT_UNKNOWN_ERROR};
const UNHANDLED_RESPONSE_TYPE_ERROR = {errorId: UNKNOWN_ERROR_ID, errorCode: ClientErrorCode.CLIENT_UNHANDLED_RESPONSE_TYPE};
const UNHANDLED_CONTENT_TYPE_ERROR = {errorId: UNKNOWN_ERROR_ID, errorCode: ClientErrorCode.CLIENT_UNHANDLED_CONTENT_TYPE};
const REDIRECT_RESPONSE_ERROR = {errorId: UNKNOWN_ERROR_ID, errorCode: ClientErrorCode.SERVER_REDIRECT_RESPONSE};

interface Client {
    get(url: string, config?: ClientRequestConfig): Promise<any>;

    post(url: string, request?: any, config?: ClientRequestConfig): Promise<any>;

    put(url: string, request?: any, config?: ClientRequestConfig): Promise<any>;

    patch(url: string, request?: any, config?: ClientRequestConfig): Promise<any>;

    delete(url: string, config?: ClientRequestConfig): Promise<any>;
}

export class RestClient implements Client {

    private static _instance: Client;
    private readonly conversationId: string;
    private readonly apiKey?: string;
    private xsrfToken?: string;

    private constructor(apiKey?: string) {
        this.apiKey = apiKey;
        this.conversationId = uuidv4();
    }

    public get(url: string, config?: ClientRequestConfig): Promise<any> {
        return this.call(RequestMethod.GET, url, config);
    }

    public post(url: string, request?: any, config?: ClientRequestConfig): Promise<any> {
        return this.call(RequestMethod.POST, url, config, request);
    }

    public put(url: string, request?: any, config?: ClientRequestConfig): Promise<any> {
        return this.call(RequestMethod.PUT, url, config, request);
    }

    public patch(url: string, request?: any, config?: ClientRequestConfig): Promise<any> {
        return this.call(RequestMethod.PATCH, url, config, request);
    }

    public delete(url: string, config?: ClientRequestConfig): Promise<any> {
        return this.call(RequestMethod.DELETE, url, config);
    }

    private call(method: RequestMethod, url: string, config?: ClientRequestConfig, request?: any): Promise<any> {
        const requestId = uuidv4();
        const endpointUrl = RestClient.buildUrl(url, config);
        const processedRequest = this.createRequest(requestId, method, config, request);
        return fetch(endpointUrl, processedRequest)
            .then(response => this.handleResponse(requestId, response));
    }

    private createRequest(requestId: string, method: RequestMethod, config?: ClientRequestConfig, body?: any): RequestInit {
        const credentials = config && config.credentialsPolicy || DEFAULT_CREDENTIALS_POLICY;
        const redirect = config && config.redirectPolicy || DEFAULT_REDIRECT_POLICY;
        const headers = this.createRequestHeaders(requestId, config);
        const defaultConfig = {
            method: method.toString(),
            credentials: credentials,
            redirect: redirect,
            headers: headers,
            body: body ? JSON.stringify(body) : null
        };
        return {...defaultConfig};
    }

    private createRequestHeaders(requestId: string, config?: ClientRequestConfig): HeadersInit {
        const headers: Headers = new Headers;
        if (!!config?.headers) {
            config.headers.forEach((key, value) => headers.set(key, value));
        }
        if (!headers.has(HeaderName.ACCEPT)) {
            headers.set(HeaderName.ACCEPT, ContentType.APPLICATION_JSON);
        }
        if (!headers.has(HeaderName.CONTENT_TYPE)) {
            headers.set(HeaderName.CONTENT_TYPE, ContentType.APPLICATION_JSON);
        }
        headers.set(HeaderName.X_CONVERSATION_ID, this.conversationId);
        headers.set(HeaderName.X_REQUEST_ID, requestId);
        if (this.apiKey) {
            headers.set(HeaderName.X_API_KEY.toString(), this.apiKey);
        }
        if (this.xsrfToken) {
            headers.set(HeaderName.X_XSRF_TOKEN.toString(), this.xsrfToken);
        }
        return headers;
    }

    private handleResponse(requestId: string, response: Response): Promise<ClientResponse> {
        switch (response.type) {
            case ResponseType.BASIC:
                return this.handleNormalResponse(this.conversationId, requestId, response);
            case ResponseType.OPAQUE_REDIRECT:
                throw this.handleResponseError(this.conversationId, requestId, response, REDIRECT_RESPONSE_ERROR);
            default:
                throw this.handleResponseError(this.conversationId, requestId, response, UNHANDLED_RESPONSE_TYPE_ERROR);
        }
    }

    private handleNormalResponse(conversationId: string, requestId: string, response: Response): Promise<ClientResponse> {
        const contentType = RestClient.extractContentTypeHeader(response);
        switch (contentType) {
            case ContentType.APPLICATION_JSON:
                return response.json()
                    .then((body) => {
                        if (response.ok) {
                            const clientResponse = this.handleResponseBody(conversationId, requestId, response, body);
                            return Promise.resolve(clientResponse);
                        } else {
                            throw this.handleResponseError(conversationId, requestId, response, body);
                        }
                    });
            case ContentType.TEXT_HTML:
            case ContentType.TEXT_PLAIN:
                if (response.ok) {
                    throw this.handleResponseError(conversationId, requestId, response, UNHANDLED_CONTENT_TYPE_ERROR);
                } else {
                    throw this.handleResponseError(conversationId, requestId, response, UNKNOWN_ERROR);
                }
            default:
                if (response.ok) {
                    const clientResponse = this.handleResponseBody(conversationId, requestId, response, undefined);
                    return Promise.resolve(clientResponse);
                } else {
                    throw this.handleResponseError(conversationId, requestId, response, UNKNOWN_ERROR);
                }
        }
    }

    private handleResponseBody(conversationId: string, requestId: string, response: Response, body?: any): ClientResponse {
        const headers = RestClient.handleResponseHeaders(response);
        const cookies = RestClient.handleResponseCookies();
        this.handleXsrfToken(cookies);
        const entityId = RestClient.extractLocationHeader(response);
        const resolvedBody = body && body.entity ? body.entity : body;
        return {
            conversationId: conversationId,
            requestId: requestId,
            entityId: entityId,
            statusCode: response.status,
            statusText: response.statusText,
            headers: headers,
            cookies: cookies,
            body: resolvedBody
        };
    }

    private handleResponseError(conversationId: string, requestId: string, response: Response, body?: any): ClientError {
        const headers = RestClient.handleResponseHeaders(response);
        const cookies = RestClient.handleResponseCookies();
        this.handleXsrfToken(cookies);
        const resolvedBody = body && body.entity ? body.entity : body;
        const {errorId, errorCode} = resolvedBody ? resolvedBody : UNKNOWN_ERROR;
        return {
            response: {
                conversationId: conversationId,
                requestId: requestId,
                errorId: errorId,
                errorCode: errorCode,
                statusCode: response.status,
                statusText: response.statusText,
                headers: headers,
                cookies: cookies,
                body: resolvedBody
            },
            error: new Error()
        };
    }

    private handleXsrfToken(cookies: Map<string, string>): void {
        const xsrfTokenCookie = cookies.get(CookieName.XSRF_TOKEN.toLowerCase());
        if (xsrfTokenCookie) {
            this.xsrfToken = xsrfTokenCookie;
        }
    }

    private static handleResponseHeaders(response: Response): Map<string, string> {
        const {headers: responseHeaders} = response;
        const headers: Map<string, string> = new Map<string, string>();
        responseHeaders.forEach((value, key) => {
            headers.set(key.toLowerCase(), value);
        });
        return headers;
    }

    private static handleResponseCookies(): Map<string, string> {
        const cookies: Map<string, string> = new Map<string, string>();
        if (document.cookie) {
            const responseCookies = document.cookie.split(/\s*;\s*/g);
            responseCookies.forEach((cookie) => {
                const nameValue = cookie.split(/\s*=\s*/g);
                const name = nameValue[0];
                const value = nameValue[1];
                cookies.set(name.toLowerCase(), value);
            });
        }
        return cookies;
    }

    private static extractContentTypeHeader(response: Response): string | undefined {
        const headers = RestClient.handleResponseHeaders(response);
        return headers.get(HeaderName.CONTENT_TYPE.toLowerCase());
    }

    private static extractLocationHeader(response: Response): string | undefined {
        const headers = RestClient.handleResponseHeaders(response);
        const location = headers.get(HeaderName.LOCATION.toLowerCase());
        const segments = location && location.split('/');
        return segments ? segments.pop() : undefined;
    }

    private static buildUrl(url: string, config?: ClientRequestConfig): string {
        if (!!config?.params && !_.isEmpty(config.params)) {
            const queryParams = RestClient.formatQueryParams(config.params);
            const endpointUrl = `${BASE_URL}/${url}?${queryParams}`;
            return encodeURI(endpointUrl);
        } else {
            const endpointUrl = `${BASE_URL}/${url}`;
            return encodeURI(endpointUrl);
        }
    }

    private static formatQueryParams(obj: {[index: string]: any}): string {
        return _.keys(obj).map((key: string) => obj[key] ? `${key}=${obj[key]}` : '').join('&');
    }

    public static getInstance() {
        return this._instance || (this._instance = new RestClient());
    }
}
