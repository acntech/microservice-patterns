import 'isomorphic-fetch';
import { ClientError, ClientMethod, ClientRequestConfig, ClientResponse } from '../../types';

const DEFAULT_METHOD: ClientMethod = 'GET';
const DEFAULT_CREDENTIALS: RequestCredentials = 'same-origin';
const DEFAULT_REDIRECT_POLICY: RequestRedirect = 'manual';
const DEFAULT_READ_HEADERS: HeadersInit = {
    'Accept': 'application/json'
}
const DEFAULT_WRITE_HEADERS: HeadersInit = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
}
const DEFAULT_FILE_WRITE_HEADERS: HeadersInit = {
    'Accept': 'application/json',
    'Content-Type': 'multipart/form-data'
}

const defaultConfig: RequestInit = {
    method: DEFAULT_METHOD,
    credentials: DEFAULT_CREDENTIALS,
    redirect: DEFAULT_REDIRECT_POLICY
}

function isFilePayload(payload: any): boolean {
    return 'File' in window && payload instanceof File;
}

function createFilePayload(payload: any): FormData {
    const formData = new FormData();
    formData.append("file", payload);
    return formData;
}

export function isClientError(e: Error): boolean {
    return e.name === 'ClientError';
}

function handleClientResponse<T = any>(response: Response, body?: any): ClientResponse<T> {
    const { status, statusText, type: responseType } = response;
    const type = responseType === 'opaqueredirect' ? 'REDIRECT' : 'NORMAL';
    return {
        type,
        body,
        status,
        statusText
    }
}

function handleClientError<T = any>(response: Response, body?: any): ClientError<T> {
    const { status, statusText } = response;
    return {
        name: 'ClientError',
        message: 'Client Error',
        response: {
            type: 'ERROR',
            body,
            status,
            statusText
        }
    }
}

async function handleSuccessResponse<T = any>(response: Response): Promise<ClientResponse<T>> {
    const body = response.ok ? await response.json() : undefined;
    return Promise.resolve(handleClientResponse(response, body));
}

async function handleErrorResponse<T = any>(response: Response): Promise<ClientResponse<T>> {
    const body = await response.json();
    return Promise.reject(handleClientError(response, body));
}

async function handleResponse<T = any>(response: Response): Promise<ClientResponse<T>> {
    if (response.status < 400) {
        return handleSuccessResponse<T>(response);
    } else {
        return handleErrorResponse<T>(response);
    }
}

async function REQUEST<T = any>(url: string, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
    config = config || {};
    return fetch(url, { ...defaultConfig, ...config })
        .then<ClientResponse<T>>(handleResponse);
}

export namespace RestClient {

    export async function GET<T = any>(url: string, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
        config = config || {};
        return REQUEST(url, { headers: DEFAULT_READ_HEADERS, ...config, method: 'GET' });
    }

    export async function DELETE<T = any>(url: string, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
        config = config || {};
        return REQUEST(url, { headers: DEFAULT_READ_HEADERS, ...config, method: 'DELETE' });
    }

    export async function PATCH<T = any>(url: string, payload?: any, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
        config = config || {};
        if (payload) {
            const body = JSON.stringify(payload);
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'PATCH', body });
        } else {
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'PATCH' });
        }
    }

    export async function POST<T = any>(url: string, payload?: any, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
        config = config || {};
        if (!payload) {
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'POST' });
        } else if (isFilePayload(payload)) {
            const body = createFilePayload(payload);
            return REQUEST(url, { headers: DEFAULT_FILE_WRITE_HEADERS, ...config, method: 'POST', body });
        } else {
            const body = JSON.stringify(payload);
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'POST', body });
        }
    }

    export async function PUT<T = any>(url: string, payload?: any, config?: ClientRequestConfig): Promise<ClientResponse<T>> {
        config = config || {};
        if (payload) {
            const body = JSON.stringify(payload);
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'PUT', body });
        } else {
            return REQUEST(url, { headers: DEFAULT_WRITE_HEADERS, ...config, method: 'PUT' });
        }
    }

}
