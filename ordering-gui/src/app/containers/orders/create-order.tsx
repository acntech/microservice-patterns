import * as React from 'react';
import {ChangeEventHandler, Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {CreateOrderForm, CreateOrderFormData, initialCreateOrderFormData, LoadingIndicator} from '../../components';

import {ActionType, CreateOrder, CustomerState, EntityType, OrderState, RootState} from '../../models';
import {createOrder} from '../../state/actions';

interface ComponentStateProps {
    customerState: CustomerState;
    orderState: OrderState;
}

interface ComponentDispatchProps {
    createOrder: (createOrder: CreateOrder) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps;

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
            return <Redirect to='/'/>;
        } else if (loading) {
            return <LoadingIndicator/>;
        } else if (modified && this.shouldRedirectToOrder()) {
            const {id: orderId} = modified;
            return <Redirect to={`/orders/${orderId}`}/>;
        } else {
            return <CreateOrderForm
                onCancelButtonClick={this.onCancelButtonClick}
                onFormSubmit={this.onFormSubmit}
                onFormInputNameChange={this.onFormInputNameChange}
                onFormTextAreaDescriptionChange={this.onFormTextAreaDescriptionChange}
                formData={formData}/>;
        }
    }

    private shouldRedirectToOrder = (): boolean => {
        const {modified} = this.props.orderState;
        const {formSubmitted} = this.state.formData;
        return formSubmitted &&
            modified != undefined &&
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

        if (!name || name.length < 2) {
            this.setFormInputNameError(formData, 'Order name must be at least 2 characters long');
            return false;
        } else if (name.length > 50) {
            this.setFormInputNameError(formData, 'Order name cannot be over 50 characters long');
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
    createOrder: (create: CreateOrder) => dispatch(createOrder(create))
});

const ConnectedCreateOrderContainer = connect(mapStateToProps, mapDispatchToProps)(CreateOrderContainer);

export {ConnectedCreateOrderContainer as CreateOrderContainer};