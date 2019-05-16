import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Container } from 'semantic-ui-react';
import { LoadingIndicator, NotFoundError, PrimaryHeader, SecondaryHeader } from '../../components';
import { ShowItem } from '../../components/orders/show-item';

import { ActionType, DeleteItem, EntityType, Item, ItemState, OrderState, Product, ProductState, RootState } from '../../models';
import { deleteItem, getOrder, getProduct } from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    orderState: OrderState;
    productState: ProductState;
    itemState: ItemState;
}

interface ComponentDispatchProps {
    getOrder: (orderId: string) => Promise<any>;
    getProduct: (productId: string) => Promise<any>;
    deleteItem: (orderId: string, item: DeleteItem) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    back: boolean;
    item?: Item;
    product?: Product;
}

const initialState: ComponentState = {
    back: false
};

class ItemContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidMount(): void {
        const {orderId, productId} = this.props.match.params;
        this.props.getOrder(orderId);
        this.props.getProduct(productId);
    }

    public componentDidUpdate(): void {
        const {orderId, productId} = this.props.match.params;
        const {loading: orderLoading} = this.props.orderState;
        const {loading: productLoading} = this.props.productState;
        const {item, product} = this.state;

        if (!orderLoading && !item && !productLoading && !product) {
            const {orders} = this.props.orderState;
            const order = orders.find(o => o.orderId === orderId);
            if (order) {
                const {products} = this.props.productState;
                const selectedItem = order.items.find(i => i.productId === productId);
                const selectedProduct = products.find(p => p.productId === productId);
                if (selectedItem && selectedProduct) {
                    this.setState({
                        item: selectedItem,
                        product: selectedProduct
                    });
                }
            }
        }
    }

    public render(): ReactNode {
        const {orderId, productId} = this.props.match.params;
        const {loading: orderLoading} = this.props.orderState;
        const {loading: productLoading} = this.props.productState;
        const {loading: itemLoading} = this.props.itemState;
        const {back, item, product} = this.state;

        if (back || this.itemDeleted()) {
            return <Redirect to={`/orders/${orderId}`} />;
        } else if (orderLoading || productLoading || itemLoading) {
            return <LoadingIndicator />;
        } else if (!item) {
            return (
                <Container className="error error-not-found">
                    <PrimaryHeader />
                    <NotFoundError
                        icon="warning sign"
                        header={{id: 'error.item-not-found.header.text'}}
                        content={{id: 'error.item-not-found.content.text', values: {orderId: orderId, productId: productId}}} />
                </Container>
            );
        } else if (!product) {
            return (
                <Container className="error error-not-found">
                    <PrimaryHeader />
                    <NotFoundError
                        icon="warning sign"
                        header={{id: 'error.product-not-found.header.text'}}
                        content={{id: 'error.product-not-found.content.text', values: {productId: productId}}} />
                </Container>
            );
        } else {
            return (
                <Container>
                    <PrimaryHeader />
                    <SecondaryHeader />
                    <ShowItem item={item}
                              product={product}
                              onBackButtonClick={this.onBackButtonClick}
                              onDeleteButtonClick={this.onDeleteButtonClick} />
                </Container>
            );
        }
    }

    private onBackButtonClick = (): void => {
        this.setState({
            back: true
        });
    };

    private onDeleteButtonClick = (): void => {
        const {orderId, productId} = this.props.match.params;
        this.props.deleteItem(orderId, {productId: productId});
    };

    private itemDeleted = (): boolean => {
        const {modified} = this.props.itemState;
        return modified !== undefined && modified.entityType === EntityType.ITEMS && modified.actionType === ActionType.DELETE;
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState,
    productState: state.productState,
    itemState: state.itemState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId)),
    getProduct: (productId: string) => dispatch(getProduct(productId)),
    deleteItem: (orderId: string, item: DeleteItem) => dispatch(deleteItem(orderId, item))
});

const ConnectedItemContainer = connect(mapStateToProps, mapDispatchToProps)(ItemContainer);

export { ConnectedItemContainer as ItemContainer };