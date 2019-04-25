import * as React from 'react';
import {Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {NotFoundErrorContainer} from '../';
import {LoadingIndicator, ShowOrder} from '../../components';

import {Order, OrderState, RootState} from '../../models';
import {getOrder} from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    orderState: OrderState;
}

interface ComponentDispatchProps {
    getOrder: (orderId: string) => Promise<any>;
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

        if (back) {
            return <Redirect to='/'/>;
        } else if (createItem) {
            return <Redirect to={`/orders/${orderId}/create`}/>;
        } else if (loading) {
            return <LoadingIndicator/>;
        } else if (!order) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No order found'
                content={`Could not find order for ID ${orderId}`}/>;
        } else {
            return <ShowOrder
                order={order}
                onBackButtonClick={this.onBackButtonClick}
                onCreateItemButtonClick={this.onCreateItemButtonClick}/>;
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
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId))
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export {ConnectedOrderContainer as OrderContainer};