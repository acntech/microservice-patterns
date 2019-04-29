import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, Icon, Segment, Table} from 'semantic-ui-react';

import {Currency, Product} from '../../models';

interface ComponentProps {
    products: Product[];
    onCancelButtonClick: () => void;
    onTableRowClick: (product: Product) => void;
}

class ShowProductListContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {products, onCancelButtonClick, onTableRowClick} = this.props;

        return (
            <Segment basic>
                <Button.Group>
                    <Button
                        secondary size='tiny'
                        onClick={onCancelButtonClick}><Icon name='arrow left'/>Back</Button>
                </Button.Group>
                <Table celled selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell>Product ID</Table.HeaderCell>
                            <Table.HeaderCell>Name</Table.HeaderCell>
                            <Table.HeaderCell>Stock</Table.HeaderCell>
                            <Table.HeaderCell>Price</Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {products.map((product, index) => {
                            const {productId, name, stock, price, currency} = product;

                            return (
                                <Table.Row key={index} className='clickable-table-row'
                                           onClick={() => onTableRowClick(product)}>
                                    <Table.Cell singleLine>{productId}</Table.Cell>
                                    <Table.Cell singleLine>{name}</Table.Cell>
                                    <Table.Cell>{stock}</Table.Cell>
                                    <Table.Cell singleLine>{Currency[currency]} {price.toFixed(2)}</Table.Cell>
                                </Table.Row>
                            );
                        })}
                    </Table.Body>
                </Table>
            </Segment>
        );
    }
}

export {ShowProductListContainer as ShowProductList};