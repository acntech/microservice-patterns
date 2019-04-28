import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import Moment from 'react-moment';
import { Button, ButtonGroup, Container, Icon, Label, Segment, Table } from 'semantic-ui-react';
import { LoadingSegment, PrimaryHeader, SecondaryHeader } from '../../components';
import { getItemStatusLabelColor, getOrderStatusLabelColor } from '../../core/utils';

import { Order, Product, ProductState } from '../../models';

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
    onRefreshOrderButtonClick: () => void;
    onFetchProducts: () => void;
}

class ShowOrderContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {order, productState, onBackButtonClick, onCreateItemButtonClick, onRefreshOrderButtonClick, onFetchProducts} = this.props;

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
                    <ButtonGroup>
                        <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}><Icon name='redo' />Refresh</Button>
                    </ButtonGroup>
                    <OrderFragment order={order} />
                    <ItemsFragment order={order} productState={productState} onFetchProducts={onFetchProducts} />
                </Segment>
            </Container>
        );
    }
}

interface OrderFragmentProps {
    order: Order;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props: OrderFragmentProps) => {
    const {orderId, name, description, status, created} = props.order;
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
                    <Table.Cell width={2}><b>Created</b></Table.Cell>
                    <Table.Cell width={10}>
                        <Moment>{created}</Moment>
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Status</b></Table.Cell>
                    <Table.Cell width={10}>
                        <Label color={statusColor}>{status}</Label>
                    </Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
};

interface ItemsFragmentProps {
    order: Order;
    productState: ProductState;
    onFetchProducts: () => void;
}

const ItemsFragment: FunctionComponent<ItemsFragmentProps> = (props: ItemsFragmentProps) => {
    const {order, productState} = props;
    const {items} = order;
    const {loading, products} = productState;

    const showItems = items.map(item => ({
        productId: item.productId,
        productName: findProductName(item.productId, products),
        quantity: item.quantity,
        status: item.status,
        statusColor: getItemStatusLabelColor(item.status)
    }));

    const loadProducts = showItems.filter(item => item.productName).length < items.length;

    if (loadProducts) {
        if (!loading) {
            props.onFetchProducts();
        }
        return <LoadingSegment />;
    } else {
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
                    {showItems.map((item, index) => {
                        const {productId, productName, quantity, status, statusColor} = item;

                        return (
                            <Table.Row key={index}>
                                <Table.Cell>{productId}</Table.Cell>
                                <Table.Cell>{productName || 'N/A'}</Table.Cell>
                                <Table.Cell>{quantity}</Table.Cell>
                                <Table.Cell>
                                    <Label color={statusColor}>{status}</Label>
                                </Table.Cell>
                            </Table.Row>
                        );
                    })}
                </Table.Body>
            </Table>
        );
    }
};

const findProductName = (productId: string, products: Product[]) => {
    return products.filter(product => product.productId === productId).map(product => product.name).pop();
};

export { ShowOrderContainer as ShowOrder };