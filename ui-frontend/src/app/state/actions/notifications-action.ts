import {
    ClearNotificationsAction, DismissNotificationAction, MessageContent, NotificationActionType, Severity, SeverityType, ShowNotification, ShowNotificationAction, Translation
} from '../../models';

const showNotificationAction = (notification: ShowNotification): ShowNotificationAction => ({type: NotificationActionType.SHOW, notification: notification});
const dismissNotificationAction = (uuid: string): DismissNotificationAction => ({type: NotificationActionType.DISMISS, uuid: uuid});
const clearNotificationsAction = (): ClearNotificationsAction => ({type: NotificationActionType.CLEAR});

export function showInfoNotification(title: Translation, content?: MessageContent, permanent?: boolean) {
    return showNotification(SeverityType.INFO, title, content, permanent);
}

export function showWarningNotification(title: Translation, content?: MessageContent, permanent?: boolean) {
    return showNotification(SeverityType.WARNING, title, content, permanent);
}

export function showErrorNotification(title: Translation, content?: MessageContent, permanent?: boolean) {
    return showNotification(SeverityType.ERROR, title, content, permanent);
}

export function showSuccessNotification(title: Translation, content?: MessageContent, permanent?: boolean) {
    return showNotification(SeverityType.SUCCESS, title, content, permanent);
}

export function showFailedNotification(title: Translation, content?: MessageContent, permanent?: boolean) {
    return showNotification(SeverityType.FAILED, title, content, permanent);
}

export function showNotification(severity: Severity, title: Translation, content?: MessageContent, permanent?: boolean) {
    return (dispatch) => {
        dispatch(showNotificationAction({severity: severity, title, content, permanent}));
    };
}

export function dismissNotification(uuid: string) {
    return (dispatch) => {
        dispatch(dismissNotificationAction(uuid));
    };
}

export function clearNotifications() {
    return (dispatch) => {
        dispatch(clearNotificationsAction());
    };
}