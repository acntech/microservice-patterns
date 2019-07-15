import { polyfill } from 'es6-promise';
import 'isomorphic-fetch';
import * as _ from 'lodash';
import uuidv4 from 'uuid/v4';

// IE Promise polyfill
polyfill();

const BASE_URL = '/api';
const DEFAULT_MODE: RequestMode = 'no-cors';
const DEFAULT_CREDENTIALS: RequestCredentials = 'same-origin';
const DEFAULT_REDIRECT_POLICY: RequestRedirect = 'follow';
const DEFAULT_HEADERS = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
};

export interface DataMap {
    [index: string]: string | number | boolean | any[];
}

enum RequestMethod {
    GET = 'GET',
    POST = 'POST',
    PUT = 'PUT',
    DELETE = 'DELETE'
}

enum ResponseCode {
    OK = 200,
    NO_CONTENT = 204,
    FOUND = 302,
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

    delete(url: string): Promise<any>;
}

export class RestClient implements Client {

    private static formatQueryParams(obj: {[index: string]: any}): string {
        return _.keys(obj).map((key: string) => !_.isUndefined(obj[key]) ? `${key}=${obj[key]}` : '').join('&');
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

    private static clientConfig(method: RequestMethod, body?: any, config?: RequestConfig): RequestInit {
        const defaultConfig = {
            method: method.toString(),
            mode: DEFAULT_MODE,
            credentials: DEFAULT_CREDENTIALS,
            redirect: DEFAULT_REDIRECT_POLICY,
            headers: {...DEFAULT_HEADERS},
            body: body ? JSON.stringify(body) : null
        };
        return {...defaultConfig, ...config};
    }

    private static handleResponse(requestId: string, response: Response): Promise<any> {
        try {
            switch (response.status) {
                case ResponseCode.OK:
                    return RestClient.handleBody(requestId, response.json());
                case ResponseCode.NO_CONTENT:
                    return Promise.resolve(null);
                case ResponseCode.FOUND:
                    const {headers} = response;
                    console.log('302!!!');
                    console.log(headers);
                    return Promise.resolve(null);
                case ResponseCode.NOT_FOUND:
                    return Promise.resolve({errorId: 'ikke.funnet', errorCode: 'ikke.funnet'});
                default:
                    console.log(response.status);
                    return RestClient.handleUnknownBody(requestId, response);
            }
        } catch (errorResponse) {
            return RestClient.handleError(requestId, 'BODY', errorResponse);
        }
    }

    private static handleBody(requestId: string, body: Promise<any>): Promise<any> {
        return body.then((responseBody) => {
            RestClient.after(requestId, responseBody);
            return Promise.resolve(responseBody.entity ? responseBody.entity : responseBody);
        }, error => RestClient.handleError(requestId, 'OK BODY', error));
    }

    private static handleUnknownBody(requestId: string, response?: Response): Promise<any> {
        return RestClient.handleError(requestId, 'UNKNOWN BODY', response);
    }

    private static handleResponseError(requestId: string, error?: any): Promise<any> {
        return RestClient.handleError(requestId, 'RESPONSE', error);
    }

    private static handleError(requestId: string, type: string, response?: any): Promise<any> {
        RestClient.after(requestId, type, response);
        return Promise.resolve({
            errorId: 'ingen.id',
            errorCode: 'ingen.kode'
        });
    };

    private static before(endpointUrl: string, clientConfig: RequestInit): string {
        const requestId = uuidv4();
        console.log(`BEFORE: ${requestId} | ${clientConfig.method} ${endpointUrl}`);
        return requestId;
    }

    private static after(requestId: string, type: string, response?: any) {
        console.log(`AFTER: ${requestId} | ${type} | `, response);
        console.table(response);
    }

    public get(url: string, config?: RequestConfig): Promise<any> {
        const endpointUrl = RestClient.buildUrl(url, config);
        const clientConfig = RestClient.clientConfig(RequestMethod.GET, null, config);
        const requestId = RestClient.before(endpointUrl, clientConfig);
        return fetch(endpointUrl, clientConfig)
            .then(response => RestClient.handleResponse(requestId, response))
            .catch(response => RestClient.handleResponseError(requestId, response));
    }

    public post(url: string, data?: any, config?: RequestConfig): Promise<any> {
        const endpointUrl = RestClient.buildUrl(url, config);
        const clientConfig = RestClient.clientConfig(RequestMethod.POST, data, config);
        const requestId = uuidv4();
        return fetch(endpointUrl, clientConfig)
            .then(response => RestClient.handleResponse(requestId, response))
            .catch(response => RestClient.handleResponseError(requestId, response));
    }

    public put(url: string, data?: any, config?: RequestConfig): Promise<any> {
        const endpointUrl = RestClient.buildUrl(url, config);
        const clientConfig = RestClient.clientConfig(RequestMethod.PUT, data, config);
        const requestId = uuidv4();
        return fetch(endpointUrl, clientConfig)
            .then(response => RestClient.handleResponse(requestId, response))
            .catch(response => RestClient.handleResponseError(requestId, response));
    }

    public delete(url: string, config?: RequestConfig): Promise<any> {
        const endpointUrl = RestClient.buildUrl(url, config);
        const clientConfig = RestClient.clientConfig(RequestMethod.DELETE, null, config);
        const requestId = uuidv4();
        return fetch(endpointUrl, clientConfig)
            .then(response => RestClient.handleResponse(requestId, response))
            .catch(response => RestClient.handleResponseError(requestId, response));
    }
}
