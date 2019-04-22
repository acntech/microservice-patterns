import * as React from 'react';
import { ChangeEventHandler, Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Container, Form, Grid, Header, Icon, Message, Segment } from 'semantic-ui-react';
import Cookies from 'universal-cookie';
import { LoadingIndicator } from '../../components';
import { Customer, CustomerState, RootState } from '../../models';
import { getCustomer, loginCustomer } from '../../state/actions';

interface ComponentStateProps {
    customerState: CustomerState;
}

interface ComponentDispatchProps {
    loginCustomer: (user: Customer) => Promise<any>;
    getCustomer: (customerId: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps;

interface FormData {
    formSubmitted: boolean;
    formError: boolean;
    formErrorMessage?: string;
    formCustomerIdValue: string;
}

interface ComponentState {
    customerId?: string;
    formData: FormData;
}

const initialState: ComponentState = {
    formData: {
        formSubmitted: false,
        formError: false,
        formCustomerIdValue: ''
    }
};

class LoginContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidMount(): void {
        const cookies = new Cookies();
        const loginCookie = cookies.get('microservice-patterns-login');
        if (loginCookie) {
            this.setState({
                ...initialState,
                customerId: loginCookie
            });
        }
    }

    public componentDidUpdate(): void {
        const {loading, user, customers} = this.props.customerState;
        const {customerId} = this.state;

        if (user) {
            this.setState(initialState);
        } else {
            if (customerId && !loading) {
                const customer = customers.find(c => c.customerId === customerId);
                if (customer) {
                    this.props.loginCustomer(customer);
                } else {
                    this.props.getCustomer(customerId);
                }
            }
        }

    }

    public render(): ReactNode {
        const {loading, user} = this.props.customerState;
        const {formData} = this.state;
        const {formSubmitted, formError, formErrorMessage} = formData;

        if (formSubmitted || loading) {
            return <LoadingIndicator />;
        } else if (user) {
            return <Redirect to='/' />;
        } else {
            return (
                <Container>
                    <Segment basic>
                        <Grid textAlign='center'>
                            <Grid.Column className='login-grid'>
                                <Header as='h1'>Login</Header>
                                <Form size='large' onSubmit={this.onFormSubmit} error={formError}>
                                    <Segment stacked>
                                        <Form.Input fluid icon='user' iconPosition='left' placeholder='Customer ID' onChange={this.onFormInputChange} error={formError} />
                                        <Form.Button primary fluid size='large'>Login</Form.Button>
                                        <Message error><Icon name='ban' /> {formErrorMessage}</Message>
                                    </Segment>
                                </Form>
                            </Grid.Column>
                        </Grid>
                    </Segment>
                </Container>
            );
        }
    }

    private onFormSubmit = (): void => {
        const {formData} = this.state;
        const {formCustomerIdValue: customerId} = formData;

        if (!customerId || customerId.length !== 36) {
            this.setState({
                formData: {
                    ...formData,
                    formSubmitted: false,
                    formError: true,
                    formErrorMessage: 'Customer ID must be a 36 characters long UUID'
                }
            });
        } else if (/\s/.test(customerId)) {
            this.setState({
                formData: {
                    ...formData,
                    formSubmitted: false,
                    formError: true,
                    formErrorMessage: 'Customer ID cannot contain any spaces'
                }
            });
        } else {
            const cookies = new Cookies();
            cookies.set('microservice-patterns-login', customerId, {path: '/'});

            this.setState({
                customerId: customerId,
                formData: {
                    ...formData,
                    formSubmitted: true,
                    formError: false
                }
            });
        }
    };

    private onFormInputChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        this.setState({
            formData: {
                ...formData,
                formSubmitted: false,
                formError: false,
                formCustomerIdValue: value
            }
        });
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    customerState: state.customerState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    loginCustomer: (user: Customer) => dispatch(loginCustomer(user)),
    getCustomer: (customerId: string) => dispatch(getCustomer(customerId))
});

const ConnectedLoginContainer = connect(mapStateToProps, mapDispatchToProps)(LoginContainer);

export { ConnectedLoginContainer as LoginContainer };