import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Container } from 'semantic-ui-react';
import { LoadingIndicator, NotFoundError, PrimaryHeader, SecondaryHeader, ShowOrder } from '../../components';

import { Order, OrderState, ProductState, RootState } from '../../models';
import { deleteOrder, findProducts, getOrder, updateOrder } from '../../state/actions';

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
    updateOrder: (orderId: string) => Promise<any>;
    deleteOrder: (orderId: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    back: boolean;
    createItem: boolean;
    order?: Order;
    productId?: string;
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

    public componentDidMount(): void {
        const {orderId} = this.props.match.params;
        this.props.getOrder(orderId);
    }

    public componentDidUpdate(): void {
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
        const {back, createItem, order, productId} = this.state;

        if (back) {
            return <Redirect to="/" />;
        } else if (createItem) {
            return <Redirect to={`/orders/${orderId}/create`} />;
        } else if (productId) {
            return <Redirect to={`/orders/${orderId}/items/${productId}`} />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (!order) {
            return (
                <Container className="error error-not-found">
                    <PrimaryHeader />
                    <NotFoundError
                        icon="warning sign"
                        header={{id: 'error.order-not-found.header.text'}}
                        content={{id: 'error.order-not-found.content.text', values: {orderId: orderId}}} />
                </Container>
            );
        } else {
            return (
                <Container>
                    <PrimaryHeader />
                    <SecondaryHeader />
                    <ShowOrder
                        order={order}
                        productState={this.props.productState}
                        onBackButtonClick={this.onBackButtonClick}
                        onCreateItemButtonClick={this.onCreateItemButtonClick}
                        onConfirmOrderButtonClick={this.onConfirmOrderButtonClick}
                        onCancelOrderButtonClick={this.onCancelOrderButtonClick}
                        onRefreshOrderButtonClick={this.onRefreshOrderButtonClick}
                        onTableRowClick={this.onTableRowClick}
                        onFetchProducts={this.onFetchProducts} />
                </Container>
            );
        }
    }

    private onBackButtonClick = (): void => {
        this.setState({
            ...initialState,
            back: true
        });
    };

    private onCreateItemButtonClick = (): void => {
        this.setState({
            ...initialState,
            createItem: true
        });
    };

    private onConfirmOrderButtonClick = (): void => {
        this.setState({
            ...initialState,
            order: undefined
        });
        const {orderId} = this.props.match.params;
        this.props.updateOrder(orderId);
    };

    private onCancelOrderButtonClick = (): void => {
        this.setState({
            ...initialState,
            order: undefined
        });
        const {orderId} = this.props.match.params;
        this.props.deleteOrder(orderId);
    };

    private onRefreshOrderButtonClick = (): void => {
        this.setState({
            ...initialState,
            order: undefined
        });
        const {orderId} = this.props.match.params;
        this.props.getOrder(orderId);
    };

    private onTableRowClick = (productId: string) => {
        this.setState({productId: productId});
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
    findProducts: () => dispatch(findProducts()),
    updateOrder: (orderId: string) => dispatch(updateOrder(orderId)),
    deleteOrder: (orderId: string) => dispatch(deleteOrder(orderId))
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };