import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Label, SemanticCOLORS, Table} from 'semantic-ui-react';
import {LoadingSegment} from '../../components';
import {getItemStatusLabelColor} from '../../core/utils';

import {Item, ItemStatus, Order, ProductState} from '../../models';

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

class ShowItemListContainer extends Component<ComponentProps> {

    componentDidMount(): void {
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
                            <Table.HeaderCell width={6}>Product ID</Table.HeaderCell>
                            <Table.HeaderCell width={8}>Product Name</Table.HeaderCell>
                            <Table.HeaderCell width={4}>Quantity</Table.HeaderCell>
                            <Table.HeaderCell width={4}>Status</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {showItems.map((item, index) => {
                            const {productId, name, quantity, status, statusColor} = item;

                            return (
                                <Table.Row key={index} className='clickable-table-row'
                                           onClick={() => onTableRowClick(productId)}>
                                    <Table.Cell>{productId}</Table.Cell>
                                    <Table.Cell>{name || 'N/A'}</Table.Cell>
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

export {ShowItemListContainer as ShowItemList};