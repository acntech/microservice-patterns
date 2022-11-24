import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';

import {OrderItem, OrderItemStatus, Product} from '../types';
import {getOrderItemStatusLabelColor} from "../core/utils";

interface FragmentProps {
    product: Product;
    orderItem: OrderItem;
    onBackButtonClick: () => void;
    onDeleteButtonClick: () => void;
    onRefreshButtonClick: () => void;
}

export const ShowOrderItemFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {
        product,
        orderItem,
        onBackButtonClick,
        onDeleteButtonClick,
        onRefreshButtonClick
    } = props;
    const {name, description, price, currency} = product;
    const {productId, quantity, status} = orderItem;

    const totalPrice = price * quantity;
    const statusColor = getOrderItemStatusLabelColor(status);
    const deleteButtonActive = status === OrderItemStatus.RESERVED || status === OrderItemStatus.REJECTED;

    return (
        <Segment basic>
            <ButtonGroup>
                <Button secondary size="tiny" onClick={onBackButtonClick}>
                    <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button negative={deleteButtonActive}
                        disabled={!deleteButtonActive}
                        size="tiny"
                        onClick={onDeleteButtonClick}>
                    <Icon name="delete"/><FormattedMessage id="button.delete"/>
                </Button>
            </ButtonGroup>
            <ButtonGroup>
                <Button secondary size='tiny' onClick={onRefreshButtonClick}>
                    <Icon name="redo"/><FormattedMessage id="button.refresh"/>
                </Button>
            </ButtonGroup>
            <Table celled>
                <Table.Body>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.product.product-id"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{productId}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.product.name"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{name}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.product.description"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{description}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order-item.status"/>
                        </Table.Cell>
                        <Table.Cell width={10}>
                            <Label color={statusColor}>
                                <FormattedMessage id={`enum.order-item-status.${status}`}/>
                            </Label>
                        </Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order-item.quantity"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{quantity}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.product.unit-price"/>
                        </Table.Cell>
                        <Table.Cell width={10}>
                            {currency} {price.toFixed(2)}
                        </Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.order-item.total-price"/>
                        </Table.Cell>
                        <Table.Cell width={10}>
                            {currency} {totalPrice.toFixed(2)}
                        </Table.Cell>
                    </Table.Row>
                </Table.Body>
            </Table>
        </Segment>
    );
};
