export enum EntityType {
    CONFIG = 'config',
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

export enum SeverityType {
    INFO = 'info',
    WARNING = 'warning',
    ERROR = 'error',
    SUCCESS = 'success',
    FAILED = 'failed'
}
