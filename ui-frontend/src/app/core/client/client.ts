import { polyfill } from 'es6-promise';
import 'isomorphic-fetch';
import * as _ from 'lodash';
import uuidv4 from 'uuid/v4';
import { ClientError, ClientResponse } from '../../models/types';

// IE Promise polyfill
polyfill();

const BASE_URL = '/api';
const DEFAULT_CREDENTIALS: RequestCredentials = 'same-origin';
const DEFAULT_REDIRECT_POLICY: RequestRedirect = 'follow';
const DEFAULT_HEADERS = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
};

export interface DataMap {
    [index: string]: string | number | boolean | any[];
}

export enum RequestMethod {
    GET = 'GET',
    POST = 'POST',
    PUT = 'PUT',
    PATCH = 'PATCH',
    DELETE = 'DELETE'
}

export enum ResponseCode {
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

export interface RequestConfig {
    headers?: any;
    params?: DataMap;
}

export interface Client {
    get(url: string): Promise<any>;

    post(url: string, data?: any): Promise<any>;

    put(url: string, data?: any): Promise<any>;

    patch(url: string, data?: any): Promise<any>;

    delete(url: string): Promise<any>;
}

export class RestClient implements Client {

    private readonly conversationId: string;

    constructor() {
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

    private call(method: RequestMethod, url: string, config?: RequestConfig, body?: any): Promise<any> {
        const requestId = uuidv4();
        const endpointUrl = RestClient.buildUrl(url, config);
        const request = RestClient.createRequest(this.conversationId, requestId, method, config, body);
        return fetch(endpointUrl, request)
            .then(response => RestClient.handleResponse(this.conversationId, requestId, response));
    }

    private static formatQueryParams(obj: {[index: string]: any}): string {
        return _.keys(obj).map((key: string) => obj[key] ? `${key}=${obj[key]}` : '').join('&');
    }

    private static buildUrl(url: string, config?: RequestConfig): string {
        if (config && config.params && !_.isEmpty(config.params)) {
            const endpointUrl = `${BASE_URL}/${url}?${RestClient.formatQueryParams(config.params)}`;
            return encodeURI(endpointUrl);
        } else {
            const endpointUrl = `${BASE_URL}/${url}`;
            return encodeURI(endpointUrl);
        }
    }

    private static extractLocationHeader(response: Response): string | undefined {
        const location = response.headers && response.headers.get('location');
        const segments = location && location.split('/');
        return segments ? segments.pop() : undefined;
    }

    private static createRequest(conversationId: string, requestId: string, method: RequestMethod, config?: RequestConfig, body?: any): RequestInit {
        const defaultConfig = {
            method: method.toString(),
            credentials: DEFAULT_CREDENTIALS,
            redirect: DEFAULT_REDIRECT_POLICY,
            headers: {
                ...DEFAULT_HEADERS,
                'X-Conversation-Id': conversationId,
                'X-Request-Id': requestId
            },
            body: body ? JSON.stringify(body) : null
        };
        return {...defaultConfig, ...config};
    }

    private static handleResponse(conversationId: string, requestId: string, response: Response): Promise<ClientResponse> {
        if (response.status === ResponseCode.CREATED) {
            return RestClient.handleBody(conversationId, requestId, response, undefined);
        }

        if (response.status === ResponseCode.NO_CONTENT) {
            return RestClient.handleBody(conversationId, requestId, response, undefined);
        }

        return response.json()
            .then((body) => {
                if (response.ok) {
                    return RestClient.handleBody(conversationId, requestId, response, body);
                } else {
                    throw RestClient.handleError(conversationId, requestId, response, body);
                }
            });
    }

    private static handleBody(conversationId: string, requestId: string, response: Response, body?: any): Promise<ClientResponse> {
        const {headers} = response;
        const resolvedBody = body && body.entity ? body.entity : body;
        const entityId = RestClient.extractLocationHeader(response);
        return Promise.resolve({
            conversationId: conversationId,
            requestId: requestId,
            entityId: entityId,
            headers: headers,
            body: resolvedBody
        });
    }

    private static handleError(conversationId: string, requestId: string, response: Response, body?: any): ClientError {
        const {headers} = response;
        const {errorId} = body ? body : {errorId: 'unknown.error.id'};
        const {errorCode} = body ? body : {errorCode: 'unknown.error.code'};
        return {
            response: {
                conversationId: conversationId,
                requestId: requestId,
                errorId: errorId,
                errorCode: errorCode,
                headers: headers,
                body: body
            },
            error: new Error()
        };
    }
}
