import * as React from 'react';
import { ChangeEventHandler, Component, FunctionComponent, ReactNode } from 'react';
import { InjectedIntlProps } from 'react-intl';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, Container, Form, Icon, InputOnChangeData, Message, Segment, TextAreaProps } from 'semantic-ui-react';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';

import { ActionType, CreateOrder, CustomerState, OrderState, RootState } from '../../models';
import { createOrder } from '../../state/actions';

interface ComponentStateProps {
    customerState: CustomerState;
    orderState: OrderState;
}

interface ComponentDispatchProps {
    createOrder: (createOrder: CreateOrder) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & InjectedIntlProps;

interface FormData {
    formError: boolean;
    formErrorMessage?: string;
    formNameValue: string;
    formDescriptionValue?: string;
}

interface ComponentState {
    cancel: boolean;
    formData: FormData;
}

const initialState: ComponentState = {
    cancel: false,
    formData: {
        formError: false,
        formNameValue: ''
    }
};

class CreateOrderContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public render(): ReactNode {
        const {cancel, formData} = this.state;
        const {orderState} = this.props;
        const {loading, modified} = orderState;

        if (cancel) {
            return <Redirect to='/' />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (modified && modified.actionType === ActionType.CREATE) {
            const {id: orderId} = modified;
            return <Redirect to={`/orders/${orderId}`} />;
        } else {
            return <CreateOrderFragment
                onCancelButtonClick={this.onCancelButtonClick}
                onFormSubmit={this.onFormSubmit}
                onFormInputChange={this.onFormInputChange}
                onFormTextAreaChange={this.onFormTextAreaChange}
                formData={formData} />;
        }
    }

    private onFormSubmit = () => {
        const {user} = this.props.customerState;
        const {customerId} = user || {customerId: ''};
        const {formData} = this.state;
        const {formNameValue: orderName, formDescriptionValue: orderDescription} = formData;
        if (!orderName || orderName.length < 2) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Order name must be at least 2 characters long'
                }
            });
        } else if (orderName.length > 50) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Order name cannot be over 50 characters long'
                }
            });
        } else {
            this.props.createOrder({
                customerId: customerId,
                name: orderName,
                description: orderDescription
            });
        }
    };

    private onFormInputChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        this.setState({
            formData: {
                ...formData,
                formError: false,
                formWarning: false,
                formNameValue: value
            }
        });
    };

    private onFormTextAreaChange: ChangeEventHandler<HTMLTextAreaElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        this.setState({
            formData: {
                ...formData,
                formError: false,
                formWarning: false,
                formDescriptionValue: value
            }
        });
    };

    private onCancelButtonClick = () => {
        this.setState({cancel: true});
    };
}

interface CreateOrderFragmentProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    onFormTextAreaChange: (event: React.SyntheticEvent<HTMLTextAreaElement>, data: TextAreaProps) => void;
    formData: FormData;
}

const CreateOrderFragment: FunctionComponent<CreateOrderFragmentProps> = (props) => {
    const {
        onCancelButtonClick,
        onFormSubmit,
        onFormInputChange,
        onFormTextAreaChange,
        formData
    } = props;
    const {
        formError,
        formErrorMessage,
        formNameValue,
        formDescriptionValue
    } = formData;

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader />
            <Segment basic>
                <Form onSubmit={onFormSubmit} error={formError}>
                    <Form.Group>
                        <Form.Input
                            error={formError}
                            width={10}
                            label='Order Name'
                            placeholder='Enter order name...'
                            value={formNameValue}
                            onChange={onFormInputChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.TextArea
                            width={10}
                            label='Order Description'
                            placeholder='Enter order description...'
                            value={formDescriptionValue}
                            onChange={onFormTextAreaChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Button
                            primary size='tiny'><Icon name='dolly' />Create Order</Form.Button>
                        <Button
                            secondary size='tiny'
                            onClick={onCancelButtonClick}><Icon name='cancel' />Cancel</Button>
                    </Form.Group>
                    <Message error><Icon name='ban' /> {formErrorMessage}</Message>
                </Form>
            </Segment>
        </Container>
    );
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    customerState: state.customerState,
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    createOrder: (create: CreateOrder) => dispatch(createOrder(create))
});

const ConnectedCreateOrderContainer = connect(mapStateToProps, mapDispatchToProps)(CreateOrderContainer);

export { ConnectedCreateOrderContainer as CreateOrderContainer };