import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, ButtonGroup, Container, Icon, Label, Segment, Table } from 'semantic-ui-react';
import { NotFoundErrorContainer } from '../';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';
import { getItemStatusLabelColor, getOrderStatusLabelColor } from '../../core/utils';

import { Order, OrderState, RootState } from '../../models';
import { getOrder } from '../../state/actions';

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
            return <OrderFragment order={order} onBackButtonClick={this.onBackButtonClick} onCreateItemButtonClick={this.onCreateItemButtonClick} />;
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

interface OrderFragmentProps {
    order: Order;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props) => {
    const {order, onBackButtonClick, onCreateItemButtonClick} = props;

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader />
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left' />Back</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button primary size='tiny' onClick={onCreateItemButtonClick}><Icon name='dolly' />New Item</Button>
                </ButtonGroup>
                <OrderDisplayFragment order={order} />
                <ItemsDisplayFragment order={order} />
            </Segment>
        </Container>
    );
};

interface OrderDisplayFragmentProps {
    order: Order;
}

const OrderDisplayFragment: FunctionComponent<OrderDisplayFragmentProps> = (props) => {
    const {orderId, name, description, status} = props.order;
    const statusColor = getOrderStatusLabelColor(status);

    return (
        <Table celled>
            <Table.Body>
                <Table.Row>
                    <Table.Cell width={2}><b>Order ID</b></Table.Cell>
                    <Table.Cell width={10}>{orderId}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Name</b></Table.Cell>
                    <Table.Cell width={10}>{name}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Description</b></Table.Cell>
                    <Table.Cell width={10}>{description}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Status</b></Table.Cell>
                    <Table.Cell width={10}><Label color={statusColor}>{status}</Label></Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
};

interface ItemsDisplayFragmentProps {
    order: Order;
}

const ItemsDisplayFragment: FunctionComponent<ItemsDisplayFragmentProps> = (props) => {
    const {items} = props.order;

    if (items && items.length > 0) {
        return (
            <Table celled>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell width={6}>Product ID</Table.HeaderCell>
                        <Table.HeaderCell width={8}>Product Name</Table.HeaderCell>
                        <Table.HeaderCell width={4}>Quantity</Table.HeaderCell>
                        <Table.HeaderCell width={4}>Status</Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {items.map((item, index) => {
                        const {productId, quantity, status: itemStatus} = item;
                        const itemStatusColor = getItemStatusLabelColor(itemStatus);

                        return (
                            <Table.Row key={index}>
                                <Table.Cell>{productId}</Table.Cell>
                                <Table.Cell>N/A</Table.Cell>
                                <Table.Cell>{quantity}</Table.Cell>
                                <Table.Cell><Label color={itemStatusColor}>{itemStatus}</Label></Table.Cell>
                            </Table.Row>
                        );
                    })}
                </Table.Body>
            </Table>
        );
    } else {
        return null;
    }
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId))
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };