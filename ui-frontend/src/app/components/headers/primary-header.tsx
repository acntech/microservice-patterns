import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { FormattedMessage, injectIntl } from 'react-intl';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { Dropdown, Header, Icon, Segment } from 'semantic-ui-react';
import Cookies from 'universal-cookie';
import { AuthenticationState, AuthenticationType, ConfigState, RootState, User } from '../../models';
import { logoutUser } from '../../state/actions';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentStateProps {
    configState: ConfigState;
    authenticationState: AuthenticationState;
}

interface ComponentDispatchProps {
    logoutUser: () => Promise<any>;
}

interface ComponentParamProps {
    title?: string;
    subtitle?: string;
}

type ComponentProps = ComponentParamProps & ComponentDispatchProps & ComponentStateProps & InjectedIntlProps;

interface ComponentState {
    logout: boolean;
}

const initialState: ComponentState = {
    logout: false
};

class PrimaryHeaderComponent extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidUpdate(): void {
        const {logout} = this.state;
        if (logout) {
            const cookies = new Cookies();
            cookies.remove('microservice-patterns-login');
            this.props.logoutUser();
        }
    }

    public render(): ReactNode {
        const {title, subtitle, configState, authenticationState, intl} = this.props;
        console.log(configState);
        const {type: securityType} = configState.config.security.authentication;
        const {user} = authenticationState.authentication;

        console.log('HREF', window.location.href);

        if (securityType === AuthenticationType.FORM_LOGIN && !user) {
            return <Redirect to="/login" />;
        } else if (securityType === AuthenticationType.OAUTH2_CLIENT && !user) {
            return <Redirect to="/login" />;
        } else {
            document.title = this.browserTitle();
            const logoutButtonText = intl.formatMessage({id: 'button.logout.text'});

            return (
                <Header className="container primary-header">
                    <Segment basic>
                        <HeaderLogoFragment title={title} subtitle={subtitle} />
                        <HeaderLoginFragment user={user} logoutButtonText={logoutButtonText} onLogoutClick={this.onLogoutClick} />
                    </Segment>
                </Header>
            );
        }
    }

    private browserTitle = (): string => {
        const {title, subtitle, intl} = this.props;
        const formattedHeaderTitle = intl.formatMessage({id: title ? title : 'primary.browser.title'});
        if (subtitle) {
            const formattedSubtitle = intl.formatMessage({id: subtitle});
            return `${formattedSubtitle} - ${formattedHeaderTitle}`;
        } else {
            return formattedHeaderTitle;
        }
    };

    private onLogoutClick = (): void => {
        this.setState({logout: true});
    };
}

interface HeaderLogoFragmentProps {
    title?: string;
    subtitle?: string;
}

const HeaderLogoFragment: FunctionComponent<HeaderLogoFragmentProps> = (props: HeaderLogoFragmentProps) => {
    const {title, subtitle} = props;
    const formattedHeaderTitle = title ? title : 'primary.header.title';

    return (
        <Header as="h1" floated="left" className="primary-header-title">
            <Link to="/">
                <Icon name="shipping fast" /> < FormattedMessage id={formattedHeaderTitle} />{subtitle ? <> - <FormattedMessage id={subtitle} /></> : null}
            </Link>
        </Header>
    );
};

interface HeaderLoginFragmentProps {
    user?: User;
    logoutButtonText: string;
    onLogoutClick: () => void;
}

const HeaderLoginFragment: FunctionComponent<HeaderLoginFragmentProps> = (props: HeaderLoginFragmentProps) => {
    const {user, logoutButtonText, onLogoutClick} = props;
    if (user) {
        const {firstName, lastName} = user;
        const name = `${firstName} ${lastName}`;

        return (
            <Header as="h3" floated="right" className="primary-header-login">
                <Icon name="user" />
                <Header.Content>
                    <Dropdown text={name}>
                        <Dropdown.Menu>
                            <Dropdown.Item text={logoutButtonText} onClick={onLogoutClick} />
                        </Dropdown.Menu>
                    </Dropdown>
                </Header.Content>
            </Header>
        );
    } else {
        return null;
    }
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    configState: state.configState,
    authenticationState: state.authenticationState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    logoutUser: () => dispatch(logoutUser())
});

const IntlPrimaryHeaderComponent = injectIntl(PrimaryHeaderComponent);

const ConnectedPrimaryHeaderComponent = connect(mapStateToProps, mapDispatchToProps)(IntlPrimaryHeaderComponent);

export { ConnectedPrimaryHeaderComponent as PrimaryHeader };