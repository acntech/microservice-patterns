import * as React from 'react';
import {Component, ReactNode} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router';
import {LoadingIndicator, ShowOrderList} from '../../components';

import {OrderState, RootState} from '../../models';
import {findOrders} from '../../state/actions';

interface ComponentStateProps {
    orderState: OrderState;
}

interface ComponentDispatchProps {
    findOrders: (name?: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps;

interface ComponentState {
    orderId?: string;
    createOrder: boolean;
}

const initialState: ComponentState = {
    createOrder: false
};

class HomeContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidMount() {
        this.props.findOrders();
    }

    public render(): ReactNode {
        const {orderId, createOrder} = this.state;
        const {orderState} = this.props;
        const {orders, loading} = orderState;

        if (orderId) {
            return <Redirect to={`/orders/${orderId}`}/>;
        } else if (loading) {
            return <LoadingIndicator/>;
        } else if (createOrder) {
            return <Redirect to='/create'/>;
        } else {
            return (
                <ShowOrderList
                    orders={orders}
                    onTableRowClick={this.onTableRowClick}
                    onCreateOrderButtonClick={this.onCreateOrderButtonClick}/>
            );
        }
    }

    private onTableRowClick = (orderId: string) => {
        this.setState({orderId: orderId});
    };

    private onCreateOrderButtonClick = () => {
        this.setState({createOrder: true});
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    findOrders: (name?: string) => dispatch(findOrders(name))
});

const ConnectedHomeContainer = connect(mapStateToProps, mapDispatchToProps)(HomeContainer);

export {ConnectedHomeContainer as HomeContainer};