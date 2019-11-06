export enum EntityType {
    CUSTOMERS = 'customers',
    PRODUCTS = 'products',
    ORDERS = 'orders',
    ITEMS = 'items'
}

export enum ActionType {
    GET = 'GET',
    FIND = 'FIND',
    CREATE = 'CREATE',
    UPDATE = 'UPDATE',
    DELETE = 'DELETE'
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

export enum HeaderName {
    ACCEPT = 'Accept',
    CONTENT_TYPE = 'Content-Type',
    X_XSRF_TOKEN = 'X-XSRF-TOKEN',
    X_CONVERSATION_ID = 'X-Conversation-Id',
    X_REQUEST_ID = 'X-Request-Id'
}

export enum CookieName {
    XSRF_TOKEN = 'XSRF-TOKEN'
}

export enum HeaderValue {
    APPLICATION_JSON = 'application/json'
}
