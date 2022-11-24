import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import {Label, SemanticCOLORS, Table} from 'semantic-ui-react';

import {Order, OrderItem, OrderItemStatus, Product} from "../types";
import {getOrderItemStatusLabelColor} from "../core/utils";

const findProductName = (productId: string, products: Product[]): string | undefined => {
    return products
        .filter(product => product.productId === productId)
        .map(product => product.name)
        .pop();
};

const enrichOrderItem = (orderItem: OrderItem, products: Product[]): ShowItem => {
    return {
        itemId: orderItem.itemId,
        productId: orderItem.productId,
        name: findProductName(orderItem.productId, products),
        quantity: orderItem.quantity,
        status: orderItem.status,
        statusColor: getOrderItemStatusLabelColor(orderItem.status)
    };
};

interface ShowItem {
    itemId: string;
    productId: string;
    name?: string;
    quantity: number;
    status: OrderItemStatus;
    statusColor: SemanticCOLORS;
}

interface FragmentProps {
    order: Order;
    products: Product[];
    onTableRowClick: (orderItem: OrderItem) => void;
}

export const ShowOrderItemListFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {order, products, onTableRowClick} = props;
    const {items} = order;

    return (
        <Table celled selectable>
            <Table.Header>
                <Table.Row>
                    <Table.HeaderCell width={6}><FormattedMessage
                        id="label.order-item.product-id"/></Table.HeaderCell>
                    <Table.HeaderCell width={6}><FormattedMessage
                        id="label.order-item.name"/></Table.HeaderCell>
                    <Table.HeaderCell width={2}><FormattedMessage
                        id="label.order-item.quantity"/></Table.HeaderCell>
                    <Table.HeaderCell width={8}><FormattedMessage
                        id="label.order-item.status"/></Table.HeaderCell>
                </Table.Row>
            </Table.Header>
            <Table.Body>
                {
                    items.map((orderItem, index) => {
                        const {productId, name, quantity, status, statusColor} = enrichOrderItem(orderItem, products);

                        return (
                            <Table.Row key={index} className="clickable-table-row"
                                       onClick={() => onTableRowClick(orderItem)}>
                                <Table.Cell>{productId}</Table.Cell>
                                <Table.Cell>{name || 'N/A'}</Table.Cell>
                                <Table.Cell>{quantity}</Table.Cell>
                                <Table.Cell>
                                    <Label color={statusColor}>
                                        <FormattedMessage id={`enum.order-item-status.${status}`}/>
                                    </Label>
                                </Table.Cell>
                            </Table.Row>
                        );
                    })
                }
            </Table.Body>
        </Table>
    );
};
