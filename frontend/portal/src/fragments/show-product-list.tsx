import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import {Button, ButtonGroup, Icon, Segment, Table} from 'semantic-ui-react';
import {Product} from "../types";

interface FragmentProps {
    products: Product[];
    onTableRowClick: (product: Product) => void;
    onBackButtonClick: () => void;
}

export const ShowProductListFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {products, onTableRowClick, onBackButtonClick} = props;

    return (
        <Segment basic>
            <ButtonGroup>
                <Button secondary size="tiny" onClick={onBackButtonClick}>
                    <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                </Button>
            </ButtonGroup>
            <Table celled selectable>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell><FormattedMessage id="label.product.product-id"/></Table.HeaderCell>
                        <Table.HeaderCell><FormattedMessage id="label.product.name"/></Table.HeaderCell>
                        <Table.HeaderCell><FormattedMessage id="label.product.stock"/></Table.HeaderCell>
                        <Table.HeaderCell><FormattedMessage id="label.product.unit-price"/></Table.HeaderCell>
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
                                <Table.Cell singleLine>{currency} {price.toFixed(2)}</Table.Cell>
                            </Table.Row>
                        );
                    })}
                </Table.Body>
            </Table>
        </Segment>
    );
};