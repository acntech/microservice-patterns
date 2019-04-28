import * as React from 'react';
import {ChangeEventHandler, Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {CreateItemForm, CreateItemFormData, initialCreateItemFormData, LoadingIndicator} from '../../components';

import {ActionType, CreateItem, EntityType, ItemState, RootState} from '../../models';
import {createItem} from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    itemState: ItemState;
}

interface ComponentDispatchProps {
    createItem: (orderId: string, create: CreateItem) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    cancel: boolean;
    formData: CreateItemFormData;
}

const initialState: ComponentState = {
    cancel: false,
    formData: initialCreateItemFormData
};

class CreateItemContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public render(): ReactNode {
        const {orderId} = this.props.match.params;
        const {loading} = this.props.itemState;
        const {cancel, formData} = this.state;

        console.log("CREATE ITEM", this.state);

        if (cancel) {
            return <Redirect to='/'/>;
        } else if (loading) {
            return <LoadingIndicator/>;
        } else if (this.shouldRedirectToOrder()) {
            return <Redirect to={`/orders/${orderId}`}/>;
        } else {
            return <CreateItemForm
                onCancelButtonClick={this.onCancelButtonClick}
                onFormSubmit={this.onFormSubmit}
                onFormInputProductIdChange={this.onFormInputProductIdChange}
                onFormInputQuantityChange={this.onFormInputQuantityChange}
                formData={formData}/>;
        }
    }

    private shouldRedirectToOrder = (): boolean => {
        const {modified} = this.props.itemState;
        const {formSubmitted} = this.state.formData;
        return formSubmitted &&
            modified != undefined &&
            modified.entityType === EntityType.ITEMS &&
            modified.actionType === ActionType.CREATE;
    };

    private onFormSubmit = () => {
        const {orderId} = this.props.match.params;
        const {formData} = this.state;
        const {formInputProductId, formInputQuantity} = formData;
        const {formElementValue: productId} = formInputProductId;
        const {formElementValue: quantity} = formInputQuantity;

        if (this.formInputProductIdIsValid(formData) && this.formInputQuantityIsValid(formData)) {
            this.setState({
                formData: {
                    ...initialCreateItemFormData,
                    formSubmitted: true
                }
            });

            this.props.createItem(
                orderId, {
                    productId: productId,
                    quantity: parseInt(quantity, 10)
                });
        }
    };

    private formInputProductIdIsValid = (formData: CreateItemFormData): boolean => {
        const {formInputProductId} = formData;
        const {formElementValue: productId} = formInputProductId;

        if (!productId || productId.length !== 36) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Product ID must be an UUID of 36 characters',
                    formInputProductId: {
                        ...formInputProductId,
                        formElementError: true
                    }
                }
            });

            return false;
        } else {
            return true;
        }
    };

    private formInputQuantityIsValid = (formData: CreateItemFormData): boolean => {
        const {formInputQuantity} = formData;
        const {formElementValue: quantity} = formInputQuantity;

        const quantityNumber = parseInt(quantity, 10);
        if (isNaN(quantityNumber) || quantityNumber < 1) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Quantity must be a positive number',
                    formInputQuantity: {
                        ...formInputQuantity,
                        formElementError: true
                    }
                }
            });

            return false;
        } else {
            return true;
        }
    };

    private onFormInputProductIdChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formInputProductId} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formInputProductId: {
                    ...formInputProductId,
                    formElementError: false,
                    formElementValue: value
                }
            }
        });
    };

    private onFormInputQuantityChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formInputQuantity} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formInputQuantity: {
                    ...formInputQuantity,
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
    itemState: state.itemState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    createItem: (orderId: string, create: CreateItem) => dispatch(createItem(orderId, create))
});

const ConnectedCreateItemContainer = connect(mapStateToProps, mapDispatchToProps)(CreateItemContainer);

export {ConnectedCreateItemContainer as CreateItemContainer};