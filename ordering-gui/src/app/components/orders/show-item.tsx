import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';

import {Currency, Item, Product} from '../../models';
import {getItemStatusLabelColor} from "../../core/utils";

interface ComponentProps {
    item: Item;
    product: Product;
    onBackButtonClick: () => void;
}

class ShowItemContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {item, product, onBackButtonClick} = this.props;
        const {productId, quantity, status} = item;
        const {name, description, price, currency} = product;
        const statusColor = getItemStatusLabelColor(status);
        const totalPrice = price * quantity;

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left'/>Back</Button>
                </ButtonGroup>
                <Table celled>
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Product ID</Table.Cell>
                            <Table.Cell width={10}>{productId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Product Name</Table.Cell>
                            <Table.Cell width={10}>{name}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Product Description</Table.Cell>
                            <Table.Cell width={10}>{description}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Status</Table.Cell>
                            <Table.Cell width={10}>
                                <Label color={statusColor}>{status}</Label>
                            </Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Quantity</Table.Cell>
                            <Table.Cell width={10}>{quantity}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Unit Price</Table.Cell>
                            <Table.Cell width={10}>
                                {Currency[currency]} {price.toFixed(2)}
                            </Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className='table-header'>Total Price</Table.Cell>
                            <Table.Cell width={10}>
                                {Currency[currency]} {totalPrice.toFixed(2)}
                            </Table.Cell>
                        </Table.Row>
                    </Table.Body>
                </Table>
            </Segment>
        );
    }
}

export {ShowItemContainer as ShowItem};