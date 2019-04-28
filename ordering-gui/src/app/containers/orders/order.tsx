import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { NotFoundErrorContainer } from '../';
import { LoadingIndicator, ShowOrder } from '../../components';

import { Order, OrderState, ProductState, RootState } from '../../models';
import { findProducts, getOrder } from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    orderState: OrderState;
    productState: ProductState;
}

interface ComponentDispatchProps {
    getOrder: (orderId: string) => Promise<any>;
    findProducts: () => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    back: boolean;
    createItem: boolean;
    order?: Order;
}

const initialState: ComponentState = {
    back: false,
    createItem: false
};

class OrderContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidMount() {
        const {orderId} = this.props.match.params;
        this.props.getOrder(orderId);
    }

    public componentDidUpdate() {
        const {orderId} = this.props.match.params;
        const {loading} = this.props.orderState;
        const {order} = this.state;

        if (!loading && !order) {
            const {orders} = this.props.orderState;
            const currentOrder = orders.find(o => o.orderId === orderId);
            if (currentOrder) {
                this.setState({order: currentOrder});
            }
        }
    }

    public render(): ReactNode {
        const {orderId} = this.props.match.params;
        const {loading} = this.props.orderState;
        const {back, createItem, order} = this.state;

        console.log("ORDER", this.state);

        if (back) {
            return <Redirect to='/' />;
        } else if (createItem) {
            return <Redirect to={`/orders/${orderId}/create`} />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (!order) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No order found'
                content={`Could not find order for ID ${orderId}`} />;
        } else {
            return <ShowOrder
                order={order}
                productState={this.props.productState}
                onBackButtonClick={this.onBackButtonClick}
                onCreateItemButtonClick={this.onCreateItemButtonClick}
                onRefreshOrderButtonClick={this.onRefreshOrderButtonClick}
                onFetchProducts={this.onFetchProducts} />;
        }
    }

    private onBackButtonClick = () => {
        this.setState({
            back: true,
            createItem: false
        });
    };

    private onCreateItemButtonClick = () => {
        this.setState({
            back: false,
            createItem: true
        });
    };

    private onRefreshOrderButtonClick = () => {
        this.setState({
            back: false,
            createItem: false,
            order: undefined
        });
        const {orderId} = this.props.match.params;
        this.props.getOrder(orderId);
    };

    private onFetchProducts = () => {
        this.props.findProducts();
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState,
    productState: state.productState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId)),
    findProducts: () => dispatch(findProducts())
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };