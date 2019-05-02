import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';

import {Currency, getItemStatusLabelColor, Item, ItemStatus, Product} from '../../models';

interface ComponentProps {
    item: Item;
    product: Product;
    onBackButtonClick: () => void;
    onDeleteButtonClick: () => void;
}

class ShowItemContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {item, product, onBackButtonClick, onDeleteButtonClick} = this.props;
        const {productId, quantity, status} = item;
        const {name, description, price, currency} = product;

        const statusColor = getItemStatusLabelColor(status);
        const totalPrice = price * quantity;
        const deleteButtonActive = this.deleteButtonActive();

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left'/>Back</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button negative={deleteButtonActive}
                            disabled={!deleteButtonActive}
                            size='tiny'
                            onClick={onDeleteButtonClick}>
                        <Icon name='delete'/>Delete</Button>
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

    private deleteButtonActive = (): boolean => {
        const {item} = this.props;
        return item.status === ItemStatus.RESERVED;
    }
}

export {ShowItemContainer as ShowItem};