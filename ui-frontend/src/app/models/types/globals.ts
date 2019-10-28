import { IntlState } from 'react-intl-redux';
import { ActionType, ConfigState, CustomerState, EntityType, ItemState, NotificationState, OrderState, ProductState, UserState } from '../';

export interface RootState {
    configState: ConfigState,
    intl: IntlState;
    notificationState: NotificationState;
    userState: UserState;
    customerState: CustomerState;
    productState: ProductState;
    orderState: OrderState;
    itemState: ItemState;
}

export interface StateError extends ErrorEntity {
    entityType: EntityType;
    actionType: ActionType;
}

export interface Modified {
    id: string;
    entityType: EntityType;
    actionType: ActionType;
}

export interface Entity {
    created: string;
    modified: string;
    loaded?: number;
}

export interface ErrorEntity {
    errorId: string;
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}

export interface ClientFields {
    conversationId: string;
    requestId: string;
}

export interface ClientResponse extends ClientFields {
    entityId?: string;
    headers: Headers;
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

export interface FormElementData {
    formElementError: boolean;
    formElementValue: string;
}

export interface FormData {
    formSubmitted: boolean;
    formError: boolean;
    formErrorMessage?: string;
}

export interface Translation {
    id: string;
    defaultMessage?: string;
    values?: {[key: string]: string}
}
