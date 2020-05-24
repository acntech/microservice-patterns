import { ReactNode } from 'react';
import { IntlState } from 'react-intl-redux';
import { ActionType, AuthenticationState, ConfigState, CustomerState, EntityType, ItemState, NotificationState, OrderState, ProductState, SeverityType } from '../';

export interface RootState {
    configState: ConfigState,
    intl: IntlState;
    notificationState: NotificationState;
    authenticationState: AuthenticationState;
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

export interface DataMap {
    [index: string]: string | number | boolean | any[];
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

export type Severity = SeverityType.INFO | SeverityType.WARNING | SeverityType.ERROR | SeverityType.SUCCESS | SeverityType.FAILED;

export type MessageContent = string | ReactNode | Translation;

export interface Message {
    severity: Severity;
    title: Translation;
    content?: MessageContent;
}
