import { ReactNode } from 'react';
import { NotificationActionType, Severity, Translation } from '../';

export interface ShowNotification {
    severity: Severity;
    title: Translation;
    content?: string | ReactNode | Translation;
    permanent?: boolean;
}

export interface Notification {
    severity: Severity;
    uuid: string;
    title: Translation;
    content?: string | ReactNode | Translation;
    permanent?: boolean;
}

export interface NotificationState {
    notifications: Notification[];
}

export interface ShowNotificationAction {
    type: NotificationActionType.SHOW,
    notification: ShowNotification
}

export interface DismissNotificationAction {
    type: NotificationActionType.DISMISS,
    uuid: string
}

export interface ClearNotificationsAction {
    type: NotificationActionType.CLEAR,
}

export type NotificationAction = ShowNotificationAction | DismissNotificationAction | ClearNotificationsAction;
