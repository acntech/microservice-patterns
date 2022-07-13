import * as React from 'react';
import {Component, ReactNode} from 'react';
import {FormattedMessage} from 'react-intl';
import {Label, SemanticCOLORS, Table} from 'semantic-ui-react';
import {LoadingSegment} from '../../components';

import {getItemStatusLabelColor, Item, ItemStatus, Order, ProductState} from '../../models';

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onTableRowClick: (productId: string) => void;
    onFetchProducts: () => void;
}

interface ShowItem {
    itemId: string;
    productId: string;
    name?: string;
    quantity: number;
    status: ItemStatus;
    statusColor: SemanticCOLORS;
}

class ShowItemListComponent extends Component<ComponentProps> {

    public componentDidMount(): void {
        this.props.onFetchProducts();
    }

    public render(): ReactNode {
        const {order, productState, onTableRowClick} = this.props;
        const {loading} = productState;
        const {items} = order;

        if (loading) {
            return <LoadingSegment/>;
        } else {
            const showItems: ShowItem[] = items.map(this.enrichItem);

            return (
                <Table celled selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell width={6}><FormattedMessage
                                id="label.productEntity-id.text"/></Table.HeaderCell>
                            <Table.HeaderCell width={6}><FormattedMessage
                                id="label.productEntity-name.text"/></Table.HeaderCell>
                            <Table.HeaderCell width={2}><FormattedMessage
                                id="label.orderItemEntity-quantity.text"/></Table.HeaderCell>
                            <Table.HeaderCell width={8}><FormattedMessage
                                id="label.orderItemEntity-status.text"/></Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {
                            showItems.map((orderItemEntity, index) => {
                                const {itemId, productId, name, quantity, status, statusColor} = orderItemEntity;

                                return (
                                    <Table.Row key={index} className="clickable-table-row"
                                               onClick={() => onTableRowClick(itemId)}>
                                        <Table.Cell>{productId}</Table.Cell>
                                        <Table.Cell>{name || 'N/A'}</Table.Cell>
                                        <Table.Cell>{quantity}</Table.Cell>
                                        <Table.Cell>
                                            <Label color={statusColor}>
                                                <FormattedMessage id={`enum.orderItemEntity-status.${status}`}/>
                                            </Label>
                                        </Table.Cell>
                                    </Table.Row>
                                );
                            })
                        }
                    </Table.Body>
                </Table>
            );
        }
    }

    private enrichItem = (orderItemEntity: Item): ShowItem => {
        return {
            itemId: orderItemEntity.itemId,
            productId: orderItemEntity.productId,
            name: this.findProductName(orderItemEntity.productId),
            quantity: orderItemEntity.quantity,
            status: orderItemEntity.status,
            statusColor: getItemStatusLabelColor(orderItemEntity.status)
        };
    };

    private findProductName = (productId: string): string | undefined => {
        const {products} = this.props.productState;
        return products.filter(productEntity => productEntity.productId === productId).map(productEntity => productEntity.name).pop();
    };
}

export {ShowItemListComponent as ShowItemList};