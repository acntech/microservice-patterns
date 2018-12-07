import * as React from 'react';
import { Component, ReactNode, FunctionComponent } from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Container, Icon } from 'semantic-ui-react';

import { Order, OrderState, RootState } from '../../models';
import { findOrders } from '../../state/actions';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';
import { NotFoundErrorContainer } from '../';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    orderState: OrderState;
}

interface ComponentDispatchProps {
    findOrders: (name?: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    order?: Order;
}

class OrderContainer extends Component<ComponentProps, ComponentState> {

    componentDidMount() {
        this.props.findOrders();
    }

    componentDidUpdate() {
        const { orderId } = this.props.match.params;
        const { order } = this.state;

        if (!order) {
            const { orders } = this.props.orderState;
            const currentOrder = orders.find(order => order.orderId == orderId);
            if (currentOrder) {
                this.setState({ order: currentOrder });
            }
        }
    }

    public render(): ReactNode {
        const { orderId } = this.props.match.params;
        const { loading } = this.props.orderState;
        const { order } = this.state;

        if (loading) {
            return <LoadingIndicator />;
        } else if (!order) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No order found'
                content={`Could not find order for ID ${orderId}`} />;
        } else {
            return <OrderFragment
                order={order} />;
        }
    }
}

interface OrderFragmentProps {
    order: Order;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props) => {
    const { order } = props;
    const { orderId, status } = order;

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader subtitle={status}>
                <Link to='/'><Icon name='home' /></Link>{'/ '}
                <Link to={`/orders/${orderId}`}>Order {orderId}</Link>
            </SecondaryHeader>
        </Container>
    );
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState,
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    findOrders: (name?: string) => dispatch(findOrders(name)),
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };