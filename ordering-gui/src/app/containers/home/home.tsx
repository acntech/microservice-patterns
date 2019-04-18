import * as React from 'react';
import { Component, ReactNode, FunctionComponent } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, Container, Icon, Segment, Table } from 'semantic-ui-react';

import { Order, OrderState, RootState } from '../../models';
import { findOrders } from '../../state/actions';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';

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

    componentDidMount() {
        this.props.findOrders();
    }

    public render(): ReactNode {
        const { orderId, createOrder } = this.state;
        const { orderState } = this.props;
        const { orders, loading } = orderState;

        if (orderId) {
            return <Redirect to={`/orders/${orderId}`} />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (createOrder) {
            return <Redirect to='/create' />;
        } else {
            return (
                <OrdersFragment
                    orders={orders}
                    onTableRowClick={this.onTableRowClick}
                    onCreateOrderButtonClick={this.onCreateOrderButtonClick} />
            );
        }
    }

    private onTableRowClick = (orderId: string) => {
        this.setState({ orderId: orderId });
    };

    private onCreateOrderButtonClick = () => {
        this.setState({ createOrder: true });
    };
}

interface OrdersFragmentProps {
    orders: Order[];
    onTableRowClick: (orderId: string) => void;
    onCreateOrderButtonClick: () => void;
}

const OrdersFragment: FunctionComponent<OrdersFragmentProps> = (props) => {
    const { orders, onTableRowClick, onCreateOrderButtonClick } = props;

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader />
            <Segment basic>
                <Button.Group>
                    <Button primary size='tiny' onClick={onCreateOrderButtonClick}>
                        <Icon name='dolly' />New Order
                    </Button>
                </Button.Group>
                <Table celled selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell width={4}>ID</Table.HeaderCell>
                            <Table.HeaderCell width={10}>Status</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {orders.map((order, index) => {
                            const { orderId, customerId, status } = order;
                            return (
                                <Table.Row key={index} className='clickable-table-row' onClick={() => onTableRowClick(orderId)}>
                                    <Table.Cell>{customerId}</Table.Cell>
                                    <Table.Cell>{status}</Table.Cell>
                                </Table.Row>
                            );
                        })}
                    </Table.Body>
                </Table>
            </Segment>
        </Container>
    );
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    findOrders: (name?: string) => dispatch(findOrders(name))
});

const ConnectedHomeContainer = connect(mapStateToProps, mapDispatchToProps)(HomeContainer);

export { ConnectedHomeContainer as HomeContainer };