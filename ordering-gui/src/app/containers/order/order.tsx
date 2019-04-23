import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, ButtonGroup, Container, Icon, Label, Segment, Table } from 'semantic-ui-react';
import { NotFoundErrorContainer } from '../';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';

import { getStatusLabelColor, Order, OrderState, RootState } from '../../models';
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
    order?: Order;
}

const initialState: ComponentState = {
    back: false
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
        const {back, order} = this.state;

        if (back) {
            return <Redirect to='/' />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (!order) {
            return <NotFoundErrorContainer
                header
                icon='warning sign'
                heading='No order found'
                content={`Could not find order for ID ${orderId}`} />;
        } else {
            return <OrderFragment
                onBackButtonClick={this.onBackButtonClick}
                order={order} />;
        }
    }

    private onBackButtonClick = () => {
        this.setState({back: true});
    };
}

interface OrderFragmentProps {
    onBackButtonClick: () => void;
    order: Order;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props) => {
    const {onBackButtonClick, order} = props;
    const {orderId, name, description, status: orderStatus, items} = order;
    const orderStatusColor = getStatusLabelColor(orderStatus);

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader />
            <Segment basic>
                <ButtonGroup>
                    <Button
                        secondary size='tiny'
                        onClick={onBackButtonClick}><Icon name='arrow left' />Back</Button>
                </ButtonGroup>
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
                            <Table.Cell width={10}><Label color={orderStatusColor}>{orderStatus}</Label></Table.Cell>
                        </Table.Row>
                    </Table.Body>
                </Table>
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
                            const { productId, quantity, status: itemStatus } = item;
                            const itemStatusColor = getStatusLabelColor(itemStatus);

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
                <ButtonGroup>
                    <Button
                        secondary size='tiny'
                        onClick={onBackButtonClick}><Icon name='arrow left' />Back</Button>
                </ButtonGroup>
            </Segment>
        </Container>
    );
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    orderState: state.orderState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId))
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };