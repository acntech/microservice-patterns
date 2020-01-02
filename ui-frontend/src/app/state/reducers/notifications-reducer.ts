import uuidv4 from 'uuid/v4';
import {
    ClearNotificationsAction,
    DismissNotificationAction,
    NotificationAction,
    NotificationActionType,
    NotificationState,
    ShowNotificationAction
} from '../../models';
import {INITIAL_NOTIFICATION_STATE} from '../store/initial-state';

export const reducer = (state: NotificationState = INITIAL_NOTIFICATION_STATE, action: NotificationAction): NotificationState => {
    switch (action.type) {
        case NotificationActionType.SHOW:
            return show(state, action);
        case NotificationActionType.DISMISS:
            return dismiss(state, action);
        case NotificationActionType.CLEAR:
            return clear(state, action);
        default:
            return state;
    }
};

const show = (state: NotificationState = INITIAL_NOTIFICATION_STATE, action: ShowNotificationAction): NotificationState => {
    switch (action.type) {
        case NotificationActionType.SHOW: {
            let {notifications} = state;
            const {notification} = action;
            notifications = notifications.concat({...notification, uuid: uuidv4()});
            return {...INITIAL_NOTIFICATION_STATE, notifications: notifications};
        }

        default: {
            return state;
        }
    }
};

const dismiss = (state: NotificationState = INITIAL_NOTIFICATION_STATE, action: DismissNotificationAction): NotificationState => {
    switch (action.type) {
        case NotificationActionType.DISMISS: {
            let {notifications} = state;
            const {uuid} = action;
            notifications = notifications.filter(notification => notification.uuid !== uuid);
            return {...INITIAL_NOTIFICATION_STATE, notifications: notifications};
        }

        default: {
            return state;
        }
    }
};

const clear = (state: NotificationState = INITIAL_NOTIFICATION_STATE, action: ClearNotificationsAction): NotificationState => {
    switch (action.type) {
        case NotificationActionType.CLEAR: {
            return INITIAL_NOTIFICATION_STATE;
        }

        default: {
            return state;
        }
    }
};
