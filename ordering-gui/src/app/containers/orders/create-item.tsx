import * as React from 'react';
import {ChangeEventHandler, Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {
    CreateItemForm,
    CreateItemFormData,
    initialCreateItemFormData,
    LoadingIndicator,
    PrimaryHeader,
    SecondaryHeader
} from '../../components';

import {ActionType, CreateItem, EntityType, ItemState, Product, ProductState, RootState} from '../../models';
import {createItem, findProducts} from '../../state/actions';
import {ShowProductList} from "../../components/orders/show-product-list";
import {Container} from "semantic-ui-react";

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    itemState: ItemState;
    productState: ProductState;
}

interface ComponentDispatchProps {
    createItem: (orderId: string, item: CreateItem) => Promise<any>;
    findProducts: () => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    cancel: boolean;
    product?: Product;
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

    componentDidMount(): void {
        this.props.findProducts();
    }

    public render(): ReactNode {
        const {orderId} = this.props.match.params;
        const {loading: itemLoading} = this.props.itemState;
        const {loading: productLoading, products} = this.props.productState;
        const {cancel, product, formData} = this.state;

        if (itemLoading || productLoading) {
            return <LoadingIndicator/>;
        } else if (cancel || this.shouldRedirectToOrder()) {
            return <Redirect to={`/orders/${orderId}`}/>;
        } else if (product) {
            return (
                <Container>
                    <PrimaryHeader/>
                    <SecondaryHeader/>
                    <CreateItemForm
                        onCancelButtonClick={this.onCancelButtonClick}
                        onFormSubmit={this.onFormSubmit}
                        onFormInputQuantityChange={this.onFormInputQuantityChange}
                        product={product}
                        formData={formData}/>
                </Container>
            );
        } else {
            return (
                <Container>
                    <PrimaryHeader/>
                    <SecondaryHeader/>
                    <ShowProductList
                        products={products}
                        onCancelButtonClick={this.onCancelButtonClick}
                        onTableRowClick={this.onTableRowClick}/>
                </Container>
            );
        }
    }

    private shouldRedirectToOrder = (): boolean => {
        const {modified} = this.props.itemState;
        const {formSubmitted} = this.state.formData;
        return formSubmitted &&
            modified !== undefined &&
            modified.entityType === EntityType.ITEMS &&
            modified.actionType === ActionType.CREATE;
    };

    private onTableRowClick = (product: Product) => {
        this.setState({product: product});
    };

    private onFormSubmit = (): void => {
        const {orderId} = this.props.match.params;
        const {product, formData} = this.state;
        const {productId} = product || {productId: ''};
        const {formInputQuantity} = formData;
        const {formElementValue: quantity} = formInputQuantity;

        if (this.formInputQuantityIsValid(formData)) {
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

    private onFormInputQuantityChange: ChangeEventHandler<HTMLInputElement> = (event): void => {
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

    private onCancelButtonClick = (): void => {
        this.setState({cancel: true});
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    itemState: state.itemState,
    productState: state.productState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    createItem: (orderId: string, item: CreateItem) => dispatch(createItem(orderId, item)),
    findProducts: () => dispatch(findProducts())
});

const ConnectedCreateItemContainer = connect(mapStateToProps, mapDispatchToProps)(CreateItemContainer);

export {ConnectedCreateItemContainer as CreateItemContainer};