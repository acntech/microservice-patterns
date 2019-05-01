import * as React from 'react';
import {Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {NotFoundErrorContainer} from '../';
import {LoadingIndicator, PrimaryHeader, SecondaryHeader} from '../../components';

import {Item, OrderState, Product, ProductState, RootState} from '../../models';
import {getOrder, getProduct} from '../../state/actions';
import {Container} from "semantic-ui-react";
import {ShowItem} from "../../components/orders/show-item";

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    orderState: OrderState;
    productState: ProductState;
}

interface ComponentDispatchProps {
    getOrder: (orderId: string) => Promise<any>;
    getProduct: (productId: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    back: boolean;
    item?: Item;
    product?: Product;
}

const initialState: ComponentState = {
    back: false,
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
                const item = order.items.find(i => i.productId === productId);
                const product = products.find(p => p.productId === productId);
                if (item && product) {
                    this.setState({
                        item: item,
                        product: product
                    });
                }
            }
        }
    }

    public render(): ReactNode {
        const {orderId, productId} = this.props.match.params;
        const {loading: orderLoading} = this.props.orderState;
        const {loading: productLoading} = this.props.productState;
        const {back, item, product} = this.state;

        if (back) {
            return <Redirect to={`/orders/${orderId}`}/>;
        } else if (orderLoading || productLoading) {
            return <LoadingIndicator/>;
        } else if (!item) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No order item found'
                content={`Could not find order item for order-id ${orderId} and product-id ${productId}`}/>;
        } else if (!product) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No product found'
                content={`Could not find product for product-id ${productId}`}/>;
        } else {
            return (
                <Container>
                    <PrimaryHeader/>
                    <SecondaryHeader/>
                    <ShowItem
                        item={item}
                        product={product}
                        onBackButtonClick={this.onBackButtonClick}/>
                </Container>
            );
        }
    }

    private onBackButtonClick = (): void => {
        this.setState({
            back: true,
        });
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState,
    productState: state.productState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId)),
    getProduct: (productId: string) => dispatch(getProduct(productId))
});

const ConnectedItemContainer = connect(mapStateToProps, mapDispatchToProps)(ItemContainer);

export {ConnectedItemContainer as ItemContainer};