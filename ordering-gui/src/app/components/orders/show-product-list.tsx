import * as React from 'react';
import { Component, ReactNode } from 'react';
import { FormattedMessage } from 'react-intl';
import { Button, Icon, Segment, Table } from 'semantic-ui-react';

import { Currency, Product } from '../../models';

interface ComponentProps {
    products: Product[];
    onCancelButtonClick: () => void;
    onTableRowClick: (product: Product) => void;
}

class ShowProductListComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {products, onCancelButtonClick, onTableRowClick} = this.props;

        return (
            <Segment basic>
                <Button.Group>
                    <Button secondary size="tiny" onClick={onCancelButtonClick}>
                        <Icon name="arrow left" /><FormattedMessage id="button.back.text" />
                    </Button>
                </Button.Group>
                <Table celled selectable>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell><FormattedMessage id="label.product-id.text" /></Table.HeaderCell>
                            <Table.HeaderCell><FormattedMessage id="label.product-name.text" /></Table.HeaderCell>
                            <Table.HeaderCell><FormattedMessage id="label.product-stock.text" /></Table.HeaderCell>
                            <Table.HeaderCell><FormattedMessage id="label.product-price.text" /></Table.HeaderCell>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {products.map((product, index) => {
                            const {productId, name, stock, price, currency} = product;

                            return (
                                <Table.Row key={index} className="clickable-table-row"
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

export { ShowProductListComponent as ShowProductList };