import * as React from 'react';
import { Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { connect } from 'react-redux';
import { Message, Segment } from 'semantic-ui-react';

import { ConfigState, NotificationState, RootState, Translation } from '../../models';
import { clearNotifications, dismissNotification } from '../../state/actions';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

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
    configState: ConfigState;
    notificationState: NotificationState;
}

interface ComponentDispatchProps {
    clearNotifications: () => Promise<any>;
    dismissNotification: (uuid: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & InjectedIntlProps;

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
                        const header = this.unwrapHeader(title);
                        const unwrappedContent = this.unwrapContent(content);

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
                            header={header}
                            content={unwrappedContent}
                            onDismiss={() => this.props.dismissNotification(uuid)} />;
                    })}
                </Segment>
            );
        } else {
            return null;
        }
    }

    private unwrapHeader(header: Translation) {
        const {values} = this.props.configState;
        const combinedValues = {...values, ...header.values};
        return this.props.intl.formatMessage({id: header.id, defaultMessage: header.defaultMessage}, combinedValues);
    }

    private unwrapContent(content?: any) {
        if (content && 'id' in content) {
            const {values} = this.props.configState;
            const combinedValues = {...values, ...content.values};
            return this.props.intl.formatMessage({id: content.id, defaultMessage: content.defaultMessage}, combinedValues);
        } else {
            return content;
        }
    }
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    configState: state.configState,
    notificationState: state.notificationState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    clearNotifications: () => dispatch(clearNotifications()),
    dismissNotification: (uuid: string) => dispatch(dismissNotification(uuid))
});

const IntlNotificationsComponent = injectIntl(NotificationsComponent);
const ConnectedNotificationsComponent = connect(mapStateToProps, mapDispatchToProps)(IntlNotificationsComponent);

export { ConnectedNotificationsComponent as Notifications };