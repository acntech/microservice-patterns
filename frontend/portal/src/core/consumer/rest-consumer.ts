import {
    ClientError,
    ClientResponse,
    CreateOrder,
    CreateOrderItem,
    ErrorPayload,
    Order,
    Product,
    SessionContext,
    UpdateOrderItem
} from "../../types";
import {RestClient} from "../client";

const authenticationAwareErrorHandler = (e: any,
                                         errorHandler: (error: ClientError<ErrorPayload>) => void) => {
    const error = e as ClientError<ErrorPayload>;

    if (error.response?.status === 401) {
        const redirectUrl = error.response.headers.get("location")
        if (redirectUrl) {
            location.replace(redirectUrl);
        }
    }
    errorHandler(error);
};

export namespace RestConsumer {

    export const getSessionContext = (successHandler: (response: ClientResponse<SessionContext>) => void,
                                      errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.GET<SessionContext>("/api/session")
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const getOrder = (orderId: string,
                             successHandler: (response: ClientResponse<Order>) => void,
                             errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.GET<Order>(`/api/orders/${orderId}`)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const getOrders = (successHandler: (response: ClientResponse<Order[]>) => void,
                              errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.GET<Order[]>("/api/orders")
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const createOrder = (body: CreateOrder,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.POST<Order>("/api/orders", body)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const updateOrder = (orderId: string,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.PUT<Order>(`/api/orders/${orderId}`, undefined)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const deleteOrder = (orderId: string,
                                successHandler: (response: ClientResponse<Order>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.DELETE<Order>(`/api/orders/${orderId}`)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const createOrderItem = (orderId: string,
                                    body: CreateOrderItem,
                                    successHandler: (response: ClientResponse<Order>) => void,
                                    errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.POST<Order>(`/api/orders/${orderId}/items`, body)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const updateOrderItem = (itemId: string,
                                    body: UpdateOrderItem,
                                    successHandler: (response: ClientResponse<Order>) => void,
                                    errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.PUT<Order>(`/api/items/${itemId}`, body)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const deleteOrderItem = (itemId: string,
                                    successHandler: (response: ClientResponse<Order>) => void,
                                    errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.DELETE<Order>(`/api/items/${itemId}`)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const getProduct = (productId: string,
                               successHandler: (response: ClientResponse<Product>) => void,
                               errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.GET<Product>(`/api/products/${productId}`)
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };

    export const getProducts = (successHandler: (response: ClientResponse<Product[]>) => void,
                                errorHandler: (error: ClientError<ErrorPayload>) => void) => {
        RestClient.GET<Product[]>("/api/products")
            .then(successHandler)
            .catch(e => authenticationAwareErrorHandler(e, errorHandler));
    };
}
