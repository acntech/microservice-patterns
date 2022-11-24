import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import {Segment, Table} from 'semantic-ui-react';
import {Product} from "../types";

interface FragmentProps {
    product: Product;
}

export const ShowProductFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {product} = props;
    const {productId, name, description, stock, currency, price} = product;

    return (
        <Segment basic>
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
                            <FormattedMessage id="label.product.stock"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{stock}</Table.Cell>
                    </Table.Row>
                    <Table.Row>
                        <Table.Cell width={2} className="table-header">
                            <FormattedMessage id="label.product.unit-price"/>
                        </Table.Cell>
                        <Table.Cell width={10}>{currency} {price.toFixed(2)}</Table.Cell>
                    </Table.Row>
                </Table.Body>
            </Table>
        </Segment>
    );
};