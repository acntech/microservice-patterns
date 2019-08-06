import { IntlState } from 'react-intl-redux';
import { ActionType, CustomerState, EntityType, ItemState, NotificationState, OrderState, ProductState, UserState } from '../';

export interface RootState {
    intl: IntlState;
    notificationState: NotificationState;
    userState: UserState;
    customerState: CustomerState;
    productState: ProductState;
    orderState: OrderState;
    itemState: ItemState;
}

export interface Error {
    entityType: EntityType;
    actionType: ActionType;
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
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
