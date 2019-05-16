import * as React from 'react';
import { ChangeEventHandler, Component, ReactNode } from 'react';
import { injectIntl } from 'react-intl';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Container } from 'semantic-ui-react';
import { CreateOrderForm, CreateOrderFormData, initialCreateOrderFormData, LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';

import { ActionType, CreateOrder, CustomerState, EntityType, OrderState, RootState } from '../../models';
import { createOrder } from '../../state/actions';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentStateProps {
    customerState: CustomerState;
    orderState: OrderState;
}

interface ComponentDispatchProps {
    createOrder: (order: CreateOrder) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & InjectedIntlProps;

interface ComponentState {
    cancel: boolean;
    formData: CreateOrderFormData;
}

const initialState: ComponentState = {
    cancel: false,
    formData: initialCreateOrderFormData
};

class CreateOrderContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public render(): ReactNode {
        const {cancel, formData} = this.state;
        const {loading, modified} = this.props.orderState;

        if (cancel) {
            return <Redirect to="/" />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (modified && this.shouldRedirectToOrder()) {
            const {id: orderId} = modified;
            return <Redirect to={`/orders/${orderId}`} />;
        } else {
            return (
                <Container>
                    <PrimaryHeader />
                    <SecondaryHeader />
                    <CreateOrderForm onCancelButtonClick={this.onCancelButtonClick}
                                     onFormSubmit={this.onFormSubmit}
                                     onFormInputNameChange={this.onFormInputNameChange}
                                     onFormTextAreaDescriptionChange={this.onFormTextAreaDescriptionChange}
                                     formData={formData} />
                </Container>
            );
        }
    }

    private shouldRedirectToOrder = (): boolean => {
        const {modified} = this.props.orderState;
        const {formSubmitted} = this.state.formData;
        return formSubmitted &&
            modified !== undefined &&
            modified.entityType === EntityType.ORDERS &&
            modified.actionType === ActionType.CREATE;
    };

    private onFormSubmit = () => {
        const {user} = this.props.customerState;
        const {customerId} = user || {customerId: ''};
        const {formData} = this.state;
        const {formInputName, formTextAreaDescription} = formData;
        const {formElementValue: name} = formInputName;
        const {formElementValue: description} = formTextAreaDescription || {formElementValue: undefined};

        if (this.formInputNameIsValid(formData)) {
            this.setState({
                formData: {
                    ...initialCreateOrderFormData,
                    formSubmitted: true
                }
            });

            this.props.createOrder({
                customerId: customerId,
                name: name,
                description: description
            });
        }
    };

    private formInputNameIsValid = (formData: CreateOrderFormData): boolean => {
        const {formInputName} = formData;
        const {formElementValue: name} = formInputName;
        const minLength = 2;
        const maxLength = 50;

        if (!name || name.length < minLength) {
            const errorMessage = this.props.intl.formatMessage({id: 'form.validation.order-name.min-length.text'}, {length: minLength});
            this.setFormInputNameError(formData, errorMessage);
            return false;
        } else if (name.length > maxLength) {
            const errorMessage = this.props.intl.formatMessage({id: 'form.validation.order-name.max-length.text'}, {length: maxLength});
            this.setFormInputNameError(formData, errorMessage);
            return false;
        } else {
            return true;
        }
    };

    private setFormInputNameError = (formData: CreateOrderFormData, errorMessage: string) => {
        const {formInputName} = formData;
        this.setState({
            formData: {
                ...formData,
                formError: true,
                formErrorMessage: errorMessage,
                formInputName: {
                    ...formInputName,
                    formElementError: true
                }
            }
        });
    };

    private onFormInputNameChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formInputName} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formInputName: {
                    ...formInputName,
                    formElementError: false,
                    formElementValue: value
                }
            }
        });
    };

    private onFormTextAreaDescriptionChange: ChangeEventHandler<HTMLTextAreaElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formTextAreaDescription} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formTextAreaDescription: {
                    ...formTextAreaDescription,
                    formElementError: false,
                    formElementValue: value
                }
            }
        });
    };

    private onCancelButtonClick = () => {
        this.setState({cancel: true});
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    customerState: state.customerState,
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    createOrder: (order: CreateOrder) => dispatch(createOrder(order))
});

const IntlCreateOrderContainer = injectIntl(CreateOrderContainer);

const ConnectedCreateOrderContainer = connect(mapStateToProps, mapDispatchToProps)(IntlCreateOrderContainer);

export { ConnectedCreateOrderContainer as CreateOrderContainer };