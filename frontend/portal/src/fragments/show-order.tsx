import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import Moment from 'react-moment';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';
import {Order, OrderItem, OrderItemStatus, OrderStatus, Product} from '../types';
import {ShowOrderItemListFragment} from "./show-order-item-list";
import {getOrderStatusLabelColor} from "../core/utils";

const isCreateOrderItemButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING;
};

const isConfirmOrderButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING &&
        order.items
            .map(item => item.status)
            .filter(status => status !== OrderItemStatus.RESERVED && status !== OrderItemStatus.CANCELED).length === 0;
};

const isCancelOrderButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING;
};

interface FragmentProps {
    order: Order;
    products: Product[];
    onBackButtonClick: () => void;
    onCreateOrderItemButtonClick: () => void;
    onConfirmOrderButtonClick: () => void;
    onCancelOrderButtonClick: () => void;
    onRefreshOrderButtonClick: () => void;
    onOrderItemTableRowClick: (orderItem: OrderItem) => void;
}

export const ShowOrderFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {
        order,
        products,
        onBackButtonClick,
        onCreateOrderItemButtonClick,
        onConfirmOrderButtonClick,
        onCancelOrderButtonClick,
        onRefreshOrderButtonClick,
        onOrderItemTableRowClick,
    } = props;
    const {orderId, name, description, status, created} = order;

    const statusColor = getOrderStatusLabelColor(status);
    const createOrderItemButtonActive = isCreateOrderItemButtonActive(order);
    const confirmOrderButtonActive = isConfirmOrderButtonActive(order);
    const cancelOrderButtonActive = isCancelOrderButtonActive(order);

    return (
        <Segment basic>
            <ButtonGroup>
                <Button secondary size="tiny" onClick={onBackButtonClick}>
                    <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button primary={createOrderItemButtonActive}
                        disabled={!createOrderItemButtonActive}
                        size="tiny" onClick={onCreateOrderItemButtonClick}>
                    <Icon name="dolly"/><FormattedMessage id="button.new-item"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button positive={confirmOrderButtonActive}
                        disabled={!confirmOrderButtonActive}
                        size="tiny" onClick={onConfirmOrderButtonClick}>
                    <Icon name="check"/><FormattedMessage id="button.confirm"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button negative={cancelOrderButtonActive}
                        disabled={!cancelOrderButtonActive}
                        size="tiny" onClick={onCancelOrderButtonClick}>
                    <Icon name="delete"/><FormattedMessage id="button.cancel"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}>
                    <Icon name="redo"/><FormattedMessage id="button.refresh"/>
                </Button>
            </ButtonGroup>

            <Table celled>
                <Table.Body>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order.order-id"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{orderId}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order.name"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{name}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order.description"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{description}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order.created-timestamp"/>
                        </Table.Cell>
                        <Table.Cell width={10}>
                            <Moment format='YYYY-MM-DD hh:mm:ss'>{created}</Moment>
                        </Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order.status"/>
                        </Table.Cell>
                        <Table.Cell width={10}>
                            <Label color={statusColor}>
                                <FormattedMessage id={`enum.order-status.${status}`}/>
                            </Label>
                        </Table.Cell>
                    </Table.Row>
                </Table.Body>
            </Table>

            <ShowOrderItemListFragment order={order}
                                       products={products}
                                       onTableRowClick={onOrderItemTableRowClick}/>
        </Segment>
    );
};
