import * as React from 'react';
import {Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Message, Segment} from 'semantic-ui-react';

import {NotificationState, RootState} from '../../models';
import {clearNotifications, dismissNotification} from '../../state/actions';

const notificationDetails = {
    info: {
        icon: 'info circle',
        info: true,
        warning: false,
        error: false,
        success: false,
        timeout: 5000
    },
    warning: {
        icon: 'warning circle',
        info: false,
        warning: true,
        error: false,
        success: false,
        timeout: 2000
    },
    error: {
        icon: 'ban',
        info: false,
        warning: false,
        error: true,
        success: false,
        timeout: 5000
    },
    success: {
        icon: 'check circle',
        info: false,
        warning: false,
        error: false,
        success: true,
        timeout: 2000
    }
};

interface ComponentStateProps {
    notificationState: NotificationState;
}

interface ComponentDispatchProps {
    clearNotifications: () => Promise<any>;
    dismissNotification: (uuid: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps;

class NotificationsComponent extends Component<ComponentProps> {

    public componentWillUnmount(): void {
        //this.props.clearNotifications();
    }

    public render(): ReactNode {
        const {notifications} = this.props.notificationState;

        if (notifications && notifications.length > 0) {
            return (
                <Segment basic className="secondary-header">
                    {notifications.map((notification, index) => {
                        const {uuid, severity, title, content, permanent} = notification;
                        const {icon, info, warning, error, success, timeout} = notificationDetails[severity];

                        if (!permanent) {
                            window.setTimeout(() => this.props.dismissNotification(uuid), timeout);
                        }

                        return <Message
                            key={index}
                            info={info}
                            warning={warning}
                            error={error}
                            success={success}
                            icon={icon}
                            header={title}
                            content={content}
                            onDismiss={() => this.props.dismissNotification(uuid)}/>;
                    })}
                </Segment>
            );
        } else {
            return null;
        }
    }
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    notificationState: state.notificationState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    clearNotifications: () => dispatch(clearNotifications()),
    dismissNotification: (uuid: string) => dispatch(dismissNotification(uuid))
});

const ConnectedNotificationsComponent = connect(mapStateToProps, mapDispatchToProps)(NotificationsComponent);

export {ConnectedNotificationsComponent as Notifications};