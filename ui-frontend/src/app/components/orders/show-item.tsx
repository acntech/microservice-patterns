import * as React from 'react';
import { Component, ReactNode } from 'react';
import { FormattedMessage } from 'react-intl';
import { Button, ButtonGroup, Icon, Label, Segment, Table } from 'semantic-ui-react';

import { Currency, getItemStatusLabelColor, Item, ItemStatus, Product } from '../../models';

interface ComponentProps {
    item: Item;
    productEntity: Product;
    onBackButtonClick: () => void;
    onDeleteButtonClick: () => void;
    onRefreshButtonClick: () => void;
}

class ShowItemComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {item, productEntity, onBackButtonClick, onDeleteButtonClick, onRefreshButtonClick} = this.props;
        const {productId, quantity, status} = item;
        const {name, description, price, currency} = productEntity;

        const statusColor = getItemStatusLabelColor(status);
        const totalPrice = price * quantity;
        const deleteButtonActive = this.deleteButtonActive();

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size="tiny" onClick={onBackButtonClick}>
                        <Icon name="arrow left" /><FormattedMessage id="button.back.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button negative={deleteButtonActive}
                        disabled={!deleteButtonActive}
                        size="tiny"
                        onClick={onDeleteButtonClick}>
                        <Icon name="delete" /><FormattedMessage id="button.delete.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onRefreshButtonClick}>
                        <Icon name="redo" /><FormattedMessage id="button.refresh.text" />
                    </Button>
                </ButtonGroup>
                <Table celled>
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.productEntity-id.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{productId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.productEntity-name.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{name}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.productEntity-description.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{description}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.item-status.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>
                                <Label color={statusColor}>
                                    <FormattedMessage id={`enum.item-status.${status}`} />
                                </Label>
                            </Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.item-quantity.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{quantity}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.item-unit-price.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>
                                {Currency[currency]} {price.toFixed(2)}
                            </Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.item-total-price.text" />
                            </Table.Cell>
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
        return item.status === ItemStatus.RESERVED || item.status === ItemStatus.REJECTED;
    };
}

export { ShowItemComponent as ShowItem };