import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Link, Redirect } from 'react-router-dom';
import { Dropdown, Header, Icon, Segment } from 'semantic-ui-react';
import Cookies from 'universal-cookie';
import { CustomerState, RootState } from '../../models';
import { logoutCustomer } from '../../state/actions';

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

type ComponentProps = ComponentParamProps & ComponentDispatchProps & ComponentStateProps;

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
        const {user} = this.props.customerState;

        if (!user) {
            return <Redirect to='/login' />;
        } else {
            const browserTitle = this.browserTitle();
            const headerTitle = this.headerTitle();
            document.title = browserTitle;
            const {firstName, lastName} = user;
            const name = `${firstName} ${lastName}`;

            return (
                <Segment basic className="primary-header">
                    <Header as='h1' floated='left' className="primary-header-title">
                        <Link to="/">
                            <Icon name='box' />{headerTitle}
                        </Link>
                    </Header>
                    <Header as='h3' floated='right' className="primary-header-login">
                        <Icon name='user' />
                        <Header.Content>
                            <Dropdown text={name}>
                                <Dropdown.Menu>
                                    <Dropdown.Item text='Logout' onClick={this.onLogoutClick} />
                                </Dropdown.Menu>
                            </Dropdown>
                        </Header.Content>
                    </Header>
                </Segment>
            );
        }
    }

    private browserTitle = (): string => {
        const {title, subtitle} = this.props;
        const formattedHeaderTitle = title ? title : 'Ordering';
        if (subtitle) {
            return `${subtitle} - ${formattedHeaderTitle}`;
        } else {
            return formattedHeaderTitle;
        }
    };

    private headerTitle = (): string => {
        const {title, subtitle} = this.props;
        const formattedHeaderTitle = title ? title : 'Ordering';
        if (subtitle) {
            return `${formattedHeaderTitle} - ${subtitle}`;
        } else {
            return formattedHeaderTitle;
        }
    };

    private onLogoutClick = (): void => {
        this.setState({logout: true});
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    customerState: state.customerState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    logoutCustomer: () => dispatch(logoutCustomer())
});

const ConnectedPrimaryHeaderComponent = connect(mapStateToProps, mapDispatchToProps)(PrimaryHeaderComponent);

export { ConnectedPrimaryHeaderComponent as PrimaryHeader };