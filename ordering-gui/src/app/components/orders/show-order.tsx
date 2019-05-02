import * as React from 'react';
import {Component, FunctionComponent, ReactNode} from 'react';
import Moment from 'react-moment';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';

import {getOrderStatusLabelColor, ItemStatus, Order, ProductState} from '../../models';
import {ShowItemList} from "./show-item-list";

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
    onRefreshOrderButtonClick: () => void;
    onTableRowClick: (productId: string) => void;
    onFetchProducts: () => void;
}

class ShowOrderContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            order,
            productState,
            onBackButtonClick,
            onCreateItemButtonClick,
            onRefreshOrderButtonClick,
            onTableRowClick,
            onFetchProducts
        } = this.props;

        const confirmButtonActive = this.confirmButtonActive();

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left'/>Back</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button primary size='tiny' onClick={onCreateItemButtonClick}><Icon name='dolly'/>New Item</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button positive={confirmButtonActive}
                            disabled={!confirmButtonActive}
                            size='tiny'>
                        <Icon name='check'/>Confirm</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}><Icon
                        name='redo'/>Refresh</Button>
                </ButtonGroup>

                <OrderFragment order={order}/>

                <ShowItemList order={order}
                              productState={productState}
                              onTableRowClick={onTableRowClick}
                              onFetchProducts={onFetchProducts}/>
            </Segment>
        );
    }

    private confirmButtonActive = (): boolean => {
        const {order} = this.props;
        return order.items.length > 0 && order.items.filter(i => i.status !== ItemStatus.CONFIRMED).length == 0;
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
                    <Table.Cell width={2} className='table-header'>Order ID</Table.Cell>
                    <Table.Cell width={10}>{orderId}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className='table-header'>Name</Table.Cell>
                    <Table.Cell width={10}>{name}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className='table-header'>Description</Table.Cell>
                    <Table.Cell width={10}>{description}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className='table-header'>Created</Table.Cell>
                    <Table.Cell width={10}>
                        <Moment>{created}</Moment>
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className='table-header'>Status</Table.Cell>
                    <Table.Cell width={10}>
                        <Label color={statusColor}>{status}</Label>
                    </Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
};

export {ShowOrderContainer as ShowOrder};