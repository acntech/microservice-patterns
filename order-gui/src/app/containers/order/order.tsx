import * as React from 'react';
import { Component, ReactNode, FunctionComponent } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, ButtonGroup, Container, Icon, Segment, Table } from 'semantic-ui-react';

import { Order, OrderState, RootState } from '../../models';
import { getOrder } from '../../state/actions';
import { LoadingIndicator, PrimaryHeader } from '../../components';
import { NotFoundErrorContainer } from '../';

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

    componentDidMount() {
        const { orderId } = this.props.match.params;
        this.props.getOrder(orderId);
    }

    componentDidUpdate() {
        const { orderId } = this.props.match.params;
        const { loading } = this.props.orderState;
        const { order } = this.state;

        if (!loading && !order) {
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
        const { back, order } = this.state;

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
        this.setState({ back: true });
    }
}

interface OrderFragmentProps {
    onBackButtonClick: () => void;
    order: Order;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props) => {
    const { onBackButtonClick, order } = props;
    const { orderId, customerId, status, created, modified } = order;

    return (
        <Container>
            <PrimaryHeader />
            <Segment basic>
            <Table celled>
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell width={2}><b>Order ID</b></Table.Cell>
                            <Table.Cell width={10}>{orderId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Customer ID</b></Table.Cell>
                            <Table.Cell width={10}>{customerId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Status</b></Table.Cell>
                            <Table.Cell width={10}>{status}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Created</b></Table.Cell>
                            <Table.Cell width={10}>{created}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Modified</b></Table.Cell>
                            <Table.Cell width={10}>{modified}</Table.Cell>
                        </Table.Row>
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
    orderState: state.orderState,
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    getOrder: (orderId: string) => dispatch(getOrder(orderId)),
});

const ConnectedOrderContainer = connect(mapStateToProps, mapDispatchToProps)(OrderContainer);

export { ConnectedOrderContainer as OrderContainer };