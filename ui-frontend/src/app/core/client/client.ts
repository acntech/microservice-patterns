import { polyfill } from 'es6-promise';
import 'isomorphic-fetch';
import * as _ from 'lodash';
import uuidv4 from 'uuid/v4';
import { ClientError, ClientResponse, CookieName, HeaderName, HeaderValue, RequestConfig, RequestMethod, ResponseCode } from '../../models';

// IE Promise polyfill
polyfill();

const BASE_URL = '/api';
const DEFAULT_CREDENTIALS: RequestCredentials = 'same-origin';
const DEFAULT_REDIRECT_POLICY: RequestRedirect = 'follow';

interface Client {
    get(url: string, config?: RequestConfig): Promise<any>;

    post(url: string, request?: any, config?: RequestConfig): Promise<any>;

    put(url: string, request?: any, config?: RequestConfig): Promise<any>;

    patch(url: string, request?: any, config?: RequestConfig): Promise<any>;

    delete(url: string, config?: RequestConfig): Promise<any>;
}

export class RestClient implements Client {

    private static _instance: Client;
    private readonly conversationId: string;
    private xsrfToken?: string;

    private constructor() {
        this.conversationId = uuidv4();
    }

    public get(url: string, config?: RequestConfig): Promise<any> {
        return this.call(RequestMethod.GET, url, config);
    }

    public post(url: string, request?: any, config?: RequestConfig): Promise<any> {
        return this.call(RequestMethod.POST, url, config, request);
    }

    public put(url: string, request?: any, config?: RequestConfig): Promise<any> {
        return this.call(RequestMethod.PUT, url, config, request);
    }

    public patch(url: string, request?: any, config?: RequestConfig): Promise<any> {
        return this.call(RequestMethod.PATCH, url, config, request);
    }

    public delete(url: string, config?: RequestConfig): Promise<any> {
        return this.call(RequestMethod.DELETE, url, config);
    }

    private call(method: RequestMethod, url: string, config?: RequestConfig, request?: any): Promise<any> {
        const requestId = uuidv4();
        const endpointUrl = RestClient.buildUrl(url, config);
        const processedRequest = this.createRequest(this.conversationId, requestId, method, this.xsrfToken, config, request);
        return fetch(endpointUrl, processedRequest)
            .then(response => this.handleResponse(this.conversationId, requestId, response));
    }

    private createRequest(conversationId: string, requestId: string, method: RequestMethod, xsrfToken?: string, config?: RequestConfig, body?: any): RequestInit {
        const headers = this.createRequestHeaders(conversationId, requestId, xsrfToken, config);
        const defaultConfig = {
            method: method.toString(),
            credentials: DEFAULT_CREDENTIALS,
            redirect: DEFAULT_REDIRECT_POLICY,
            headers: headers,
            body: body ? JSON.stringify(body) : null
        };
        return {...defaultConfig};
    }

    private createRequestHeaders(conversationId: string, requestId: string, xsrfToken?: string, config?: RequestConfig): HeadersInit {
        const headers: Headers = new Headers;
        if (config && config.headers) {
            config.headers.forEach((key, value) => headers.set(key, value));
        }
        if (!headers.has(HeaderName.ACCEPT)) {
            headers.set(HeaderName.ACCEPT, HeaderValue.APPLICATION_JSON);
        }
        if (!headers.has(HeaderName.CONTENT_TYPE)) {
            headers.set(HeaderName.CONTENT_TYPE, HeaderValue.APPLICATION_JSON);
        }
        headers.set(HeaderName.X_CONVERSATION_ID, conversationId);
        headers.set(HeaderName.X_REQUEST_ID, requestId);
        if (xsrfToken) {
            headers.set(HeaderName.X_XSRF_TOKEN.toString(), xsrfToken);
        }
        return headers;
    }

    private handleResponse(conversationId: string, requestId: string, response: Response): Promise<ClientResponse> {
        if (response.status === ResponseCode.CREATED) {
            return this.handleResponseBody(conversationId, requestId, response, undefined);
        }

        if (response.status === ResponseCode.NO_CONTENT) {
            return this.handleResponseBody(conversationId, requestId, response, undefined);
        }

        return response.json()
            .then((body) => {
                if (response.ok) {
                    return this.handleResponseBody(conversationId, requestId, response, body);
                } else {
                    throw this.handleResponseError(conversationId, requestId, response, body);
                }
            });
    }

    private handleResponseBody(conversationId: string, requestId: string, response: Response, body?: any): Promise<ClientResponse> {
        const headers = this.handleResponseHeaders(response);
        const cookies = this.handleResponseCookies();
        const resolvedBody = body && body.entity ? body.entity : body;
        const entityId = RestClient.extractLocationHeader(response);
        return Promise.resolve({
            conversationId: conversationId,
            requestId: requestId,
            entityId: entityId,
            headers: headers,
            cookies: cookies,
            body: resolvedBody
        });
    }

    private handleResponseError(conversationId: string, requestId: string, response: Response, body?: any): ClientError {
        const headers = this.handleResponseHeaders(response);
        const cookies = this.handleResponseCookies();
        const resolvedBody = body && body.entity ? body.entity : body;
        const {errorId} = resolvedBody ? resolvedBody : {errorId: 'unknown.error.id'};
        const {errorCode} = resolvedBody ? resolvedBody : {errorCode: 'unknown.error.code'};
        return {
            response: {
                conversationId: conversationId,
                requestId: requestId,
                errorId: errorId,
                errorCode: errorCode,
                headers: headers,
                cookies: cookies,
                body: resolvedBody
            },
            error: new Error()
        };
    }

    private handleResponseHeaders(response: Response): Map<string, string> {
        const {headers: responseHeaders} = response;
        const headers: Map<string, string> = new Map<string, string>();
        responseHeaders.forEach((value, key) => {
            headers.set(key, value);
        });
        return headers;
    }

    private handleResponseCookies(): Map<string, string> {
        const cookies: Map<string, string> = new Map<string, string>();
        if (document.cookie) {
            const responseCookies = document.cookie.split(/\s*;\s*/g);
            responseCookies.forEach((cookie) => {
                const nameValue = cookie.split(/\s*=\s*/g);
                const name = nameValue[0];
                const value = nameValue[1];
                cookies.set(name, value);
                if (name === CookieName.XSRF_TOKEN && value) {
                    this.xsrfToken = value;
                }
            });
        }
        return cookies;
    }

    private static buildUrl(url: string, config?: RequestConfig): string {
        if (config && config.params && !_.isEmpty(config.params)) {
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

    private static extractLocationHeader(response: Response): string | undefined {
        const location = response.headers && response.headers.get('location');
        const segments = location && location.split('/');
        return segments ? segments.pop() : undefined;
    }

    public static getInstance() {
        return this._instance || (this._instance = new RestClient());
    }
}
