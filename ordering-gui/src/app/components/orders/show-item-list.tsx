import * as React from 'react';
import { Component, ReactNode } from 'react';
import { FormattedMessage } from 'react-intl';
import { Label, SemanticCOLORS, Table } from 'semantic-ui-react';
import { LoadingSegment } from '../../components';

import { getItemStatusLabelColor, Item, ItemStatus, Order, ProductState } from '../../models';

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onTableRowClick: (productId: string) => void;
    onFetchProducts: () => void;
}

interface ShowItem {
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
            return <LoadingSegment />;
        } else {
            const showItems: ShowItem[] = items.map(this.enrichItem);

            return (
                <Table celled selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell width={6}><FormattedMessage id="label.product-id.text" /></Table.HeaderCell>
                            <Table.HeaderCell width={6}><FormattedMessage id="label.product-name.text" /></Table.HeaderCell>
                            <Table.HeaderCell width={2}><FormattedMessage id="label.item-quantity.text" /></Table.HeaderCell>
                            <Table.HeaderCell width={8}><FormattedMessage id="label.item-status.text" /></Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {showItems.map((item, index) => {
                            const {productId, name, quantity, status, statusColor} = item;

                            return (
                                <Table.Row key={index} className="clickable-table-row"
                                    onClick={() => onTableRowClick(productId)}>
                                    <Table.Cell>{productId}</Table.Cell>
                                    <Table.Cell>{name || 'N/A'}</Table.Cell>
                                    <Table.Cell>{quantity}</Table.Cell>
                                    <Table.Cell>
                                        <Label color={statusColor}>
                                            <FormattedMessage id={`enum.item-status.${status}`} />
                                        </Label>
                                    </Table.Cell>
                                </Table.Row>
                            );
                        })}
                    </Table.Body>
                </Table>
            );
        }
    }

    private enrichItem = (item: Item): ShowItem => {
        return {
            productId: item.productId,
            name: this.findProductName(item.productId),
            quantity: item.quantity,
            status: item.status,
            statusColor: getItemStatusLabelColor(item.status)
        };
    };

    private findProductName = (productId: string): string | undefined => {
        const {products} = this.props.productState;
        return products.filter(product => product.productId === productId).map(product => product.name).pop();
    };
}

export { ShowItemListComponent as ShowItemList };