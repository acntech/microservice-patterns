import * as React from 'react';
import {ChangeEventHandler, Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {Action} from 'redux';
import {ThunkDispatch} from 'redux-thunk';
import {Container, Form, Grid, Header, List, Message, Segment} from 'semantic-ui-react';
import Cookies from 'universal-cookie';
import {LoadingIndicator} from '../../components';
import {CustomerQuery, CustomerState, RootState, User, UserState} from '../../models';
import {findCustomers, getCustomer, loginUser} from '../../state/actions';

interface ComponentStateProps {
    userState: UserState;
    customerState: CustomerState;
}

interface ComponentDispatchProps {
    loginUser: (user: User) => Promise<any>;
    getCustomer: (customerId: string) => Promise<any>;
    findCustomers: (query?: CustomerQuery) => Promise<any>;
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
        const {user} = this.props.userState;
        if (!user) {
            this.props.findCustomers();
            const cookies = new Cookies();
            const loginCookie = cookies.get('microservice-patterns-login');
            if (loginCookie) {
                this.setState({
                    ...initialState,
                    customerId: loginCookie
                });
            }
        }
    }

    public componentDidUpdate(): void {
        const {user} = this.props.userState;
        const {loading, error, customers} = this.props.customerState;
        const {customerId, formData} = this.state;
        const {formSubmitted} = formData;

        if (user) {
            this.setState(initialState);
        } else if (customerId && !loading && (!error || formSubmitted)) {
            const customerEntity = customers.find(c => c.customerId === customerId);

            if (customerEntity) {
                const newUser = {
                    userId: customerEntity.customerId,
                    firstName: customerEntity.firstName,
                    lastName: customerEntity.lastName
                };
                this.props.loginUser(newUser);
            }

            this.setState(initialState);
        }
    }

    public render(): ReactNode {
        const {user} = this.props.userState;
        const {loading, customers, error} = this.props.customerState;
        const {formCustomerIdValue} = this.state.formData;
        let {formError, formErrorMessage} = this.state.formData;
        let formWarning = false;

        if (error) {
            formError = true;
            formErrorMessage = error.message;
            formWarning = true;
        }

        if (!error && loading) {
            return <LoadingIndicator/>;
        } else if (user) {
            return <Redirect to="/"/>;
        } else {
            return (
                <Container>
                    <Segment basic>
                        <Grid textAlign="center">
                            <Grid.Row>
                                <Grid.Column className="login" textAlign="center">
                                    <Header as="h1">Login</Header>
                                    <Form size="large" onSubmit={this.onFormSubmit} error={formError}
                                          warning={formWarning}>
                                        <Segment basic>
                                            <Form.Input fluid icon="user" iconPosition="left" placeholder='Customer ID'
                                                        value={formCustomerIdValue} onChange={this.onFormInputChange}/>
                                            <Form.Button primary fluid size="large">Login</Form.Button>
                                            <Message error icon="ban" content={formErrorMessage}/>
                                            <Message warning icon="warning sign"
                                                     content='customerEntity-id might come from the login cookie'/>
                                        </Segment>
                                    </Form>
                                </Grid.Column>
                            </Grid.Row>
                            <Grid.Row>
                                <Grid.Column className="login" textAlign="left">
                                    <List divided selection>
                                        {
                                            customers.map((customerEntity, index) => {
                                                const {customerId, firstName, lastName} = customerEntity;
                                                const active = customerId === formCustomerIdValue;

                                                return (
                                                    <List.Item key={index} className="login" active={active}
                                                               onClick={() => this.onListItemClick(customerId)}>
                                                        <List.Content>
                                                            <List.Header><h3>{firstName} {lastName}</h3></List.Header>
                                                            <List.Description>Customer
                                                                ID: {customerId}</List.Description>
                                                        </List.Content>
                                                    </List.Item>
                                                );
                                            })
                                        }
                                    </List>
                                </Grid.Column>
                            </Grid.Row>
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

    private onListItemClick = (customerId: string) => {
        const {formData} = this.state;
        this.setState({
            formData: {
                ...formData,
                formSubmitted: false,
                formError: false,
                formCustomerIdValue: customerId
            }
        });
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    userState: state.userState,
    customerState: state.customerState
});

const mapDispatchToProps = (dispatch: ThunkDispatch<RootState, void, Action>): ComponentDispatchProps => ({
    loginUser: (user: User) => dispatch(loginUser(user)),
    getCustomer: (customerId: string) => dispatch(getCustomer(customerId)),
    findCustomers: (query?: CustomerQuery) => dispatch(findCustomers(query))
});

const ConnectedLoginContainer = connect(mapStateToProps, mapDispatchToProps)(LoginContainer);

export {ConnectedLoginContainer as LoginContainer};