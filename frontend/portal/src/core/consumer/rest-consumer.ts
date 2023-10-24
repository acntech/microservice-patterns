import {
    ClientError,
    ClientResponse,
    CreateOrder,
    CreateOrderItem,
    ErrorPayload,
    Order,
    Product,
    SessionContext
} from "../../types";
import {RestClient} from "../client";

const authenticationAwareErrorHandler = (error: ClientError<ErrorPayload>,
                                         errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    if (error.response?.status === 401) {
        const redirectUrl = error.response.headers.get("location")
        if (redirectUrl) {
            location.replace(redirectUrl);
        }
    }
    errorHandler(error);
};

const GET = <T>(url: string,
                successHandler: (response: ClientResponse<T>) => void,
                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    RestClient.GET<T>(url)
        .then(successHandler)
        .catch(e => {
            const error = e as ClientError<ErrorPayload>;
            authenticationAwareErrorHandler(error, errorHandler);
        });
};

const POST = <T>(url: string,
                 body: any,
                 successHandler: (response: ClientResponse<T>) => void,
                 errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    RestClient.POST<T>(url, body)
        .then(successHandler)
        .catch(e => {
            const error = e as ClientError<ErrorPayload>;
            authenticationAwareErrorHandler(error, errorHandler);
        });
};

const PUT = <T>(url: string,
                body: any,
                successHandler: (response: ClientResponse<T>) => void,
                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    RestClient.PUT<T>(url, body)
        .then(successHandler)
        .catch(e => {
            const error = e as ClientError<ErrorPayload>;
            authenticationAwareErrorHandler(error, errorHandler);
        });
};

const DELETE = <T>(url: string,
                   successHandler: (response: ClientResponse<T>) => void,
                   errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    RestClient.DELETE<T>(url)
        .then(successHandler)
        .catch(e => {
            const error = e as ClientError<ErrorPayload>;
            authenticationAwareErrorHandler(error, errorHandler);
        });
};

export namespace RestConsumer {

    export const getSessionContext = (successHandler: (response: ClientResponse<SessionContext>) => void,
                                      errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        GET<SessionContext>('/api/session', successHandler, errorHandler);
    };

    export const getOrder = (orderId: string,
                             successHandler: (response: ClientResponse<Order>) => void,
                             errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        GET<Order>(`/api/orders/${orderId}`, successHandler, errorHandler);
    };

    export const getOrders = (successHandler: (response: ClientResponse<Order[]>) => void,
                              errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        GET<Order[]>('/api/orders', successHandler, errorHandler);
    };

    export const createOrder = (body: CreateOrder,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        POST<Order>('/api/orders', body, successHandler, errorHandler);
    };

    export const updateOrder = (orderId: string,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        PUT<Order>(`/api/orders/${orderId}`, undefined, successHandler, errorHandler);
    };

    export const deleteOrder = (orderId: string,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        DELETE<Order>(`/api/orders/${orderId}`, successHandler, errorHandler);
    };

    export const createOrderItem = (orderId: string,
                                    body: CreateOrderItem,
                                    successHandler: (response: ClientResponse<Order>) => void,
                                    errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        POST<Order>(`/api/orders/${orderId}/items`, body, successHandler, errorHandler);
    };

    export const deleteOrderItem = (itemId: string,
                                    successHandler: (response: ClientResponse<Order>) => void,
                                    errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        DELETE<Order>(`/api/items/${itemId}`, successHandler, errorHandler);
    };

    export const getProduct = (productId: string,
                               successHandler: (response: ClientResponse<Product>) => void,
                               errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        GET<Product>(`/api/products/$${productId}`, successHandler, errorHandler);
    };

    export const getProducts = (successHandler: (response: ClientResponse<Product[]>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        GET<Product[]>('/api/products', successHandler, errorHandler);
    };
}
