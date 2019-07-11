import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { FormattedMessage, injectIntl } from 'react-intl';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { Dropdown, Header, Icon, Segment } from 'semantic-ui-react';
import Cookies from 'universal-cookie';
import { globalConfig } from '../../core/config';
import { CustomerState, RootState } from '../../models';
import { logoutCustomer } from '../../state/actions';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentStateProps {
    customerState: CustomerState;
}

interface ComponentDispatchProps {
    logoutCustomer: () => Promise<any>;
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
            this.props.logoutCustomer();
        }
    }

    public render(): ReactNode {
        const {title, subtitle, customerState, intl} = this.props;
        const {user} = customerState;

        if (globalConfig.enableSimpleLogin && !user) {
            return <Redirect to="/login" />;
        } else {
            document.title = this.browserTitle();
            const logoutButtonText = intl.formatMessage({id: 'button.logout.text'});
            const {firstName, lastName} = user || {firstName: 'N/A', lastName: 'N/A'};
            const name = `${firstName} ${lastName}`;

            return (
                <Segment basic className="primary-header">
                    <Header as="h1" floated="left" className="primary-header-title">
                        <Link to="/">
                            <Icon name="box" /> <HeaderTitle title={title} subtitle={subtitle} />
                        </Link>
                    </Header>
                    <Header as="h3" floated="right" className="primary-header-login">
                        <Icon name="user" />
                        <Header.Content>
                            <Dropdown text={name}>
                                <Dropdown.Menu>
                                    <Dropdown.Item text={logoutButtonText} onClick={this.onLogoutClick} />
                                </Dropdown.Menu>
                            </Dropdown>
                        </Header.Content>
                    </Header>
                </Segment>
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

interface HeaderTitleProps {
    title?: string;
    subtitle?: string;
}

const HeaderTitle: FunctionComponent<HeaderTitleProps> = (props: HeaderTitleProps) => {
    const {title, subtitle} = props;

    const formattedHeaderTitle = title ? title : 'primary.header.title';
    if (subtitle) {
        return <><FormattedMessage id={formattedHeaderTitle} /> - <FormattedMessage id={subtitle} /></>;
    } else {
        return <FormattedMessage id={formattedHeaderTitle} />;
    }
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    customerState: state.customerState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    logoutCustomer: () => dispatch(logoutCustomer())
});

const IntlPrimaryHeaderComponent = injectIntl(PrimaryHeaderComponent);

const ConnectedPrimaryHeaderComponent = connect(mapStateToProps, mapDispatchToProps)(IntlPrimaryHeaderComponent);

export { ConnectedPrimaryHeaderComponent as PrimaryHeader };