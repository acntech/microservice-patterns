import * as React from 'react';
import { Component, FunctionComponent, ReactNode } from 'react';
import { FormattedMessage } from 'react-intl';
import Moment from 'react-moment';
import { Button, ButtonGroup, Icon, Label, Segment, Table } from 'semantic-ui-react';

import { getOrderStatusLabelColor, ItemStatus, Order, OrderStatus, ProductState } from '../../models';
import { ShowItemList } from './show-item-list';

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
    onConfirmOrderButtonClick: () => void;
    onCancelOrderButtonClick: () => void;
    onRefreshOrderButtonClick: () => void;
    onTableRowClick: (productId: string) => void;
    onFetchProducts: () => void;
}

class ShowOrderComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            order,
            productState,
            onBackButtonClick,
            onCreateItemButtonClick,
            onConfirmOrderButtonClick,
            onCancelOrderButtonClick,
            onRefreshOrderButtonClick,
            onTableRowClick,
            onFetchProducts
        } = this.props;

        const createItemButtonActive = this.createItemButtonActive();
        const confirmOrderButtonActive = this.confirmOrderButtonActive();
        const cancelOrderButtonActive = this.cancelOrderButtonActive();

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size="tiny" onClick={onBackButtonClick}>
                        <Icon name="arrow left" /><FormattedMessage id="button.back.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button primary={createItemButtonActive}
                        disabled={!createItemButtonActive}
                        size="tiny" onClick={onCreateItemButtonClick}>
                        <Icon name="dolly" /><FormattedMessage id="button.new-item.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button positive={confirmOrderButtonActive}
                        disabled={!confirmOrderButtonActive}
                        size="tiny" onClick={onConfirmOrderButtonClick}>
                        <Icon name="check" /><FormattedMessage id="button.confirm.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button negative={cancelOrderButtonActive}
                        disabled={!cancelOrderButtonActive}
                        size="tiny" onClick={onCancelOrderButtonClick}>
                        <Icon name="delete" /><FormattedMessage id="button.cancel.text" />
                    </Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}>
                        <Icon name="redo" /><FormattedMessage id="button.refresh.text" />
                    </Button>
                </ButtonGroup>

                <OrderFragment order={order} />

                <ShowItemList order={order}
                    productState={productState}
                    onTableRowClick={onTableRowClick}
                    onFetchProducts={onFetchProducts} />
            </Segment>
        );
    }

    private createItemButtonActive = (): boolean => {
        const {order} = this.props;
        return order.status === OrderStatus.PENDING;
    };

    private confirmOrderButtonActive = (): boolean => {
        const {order} = this.props;
        return order.status === OrderStatus.PENDING &&
            order.items.length > 0 &&
            order.items.filter(i => i.status !== ItemStatus.RESERVED && i.status !== ItemStatus.CANCELED).length === 0;
    };

    private cancelOrderButtonActive = (): boolean => {
        const {order} = this.props;
        return order.status === OrderStatus.PENDING;
    };
}

interface OrderFragmentProps {
    order: Order;
}

const OrderFragment: FunctionComponent<OrderFragmentProps> = (props: OrderFragmentProps) => {
    const {orderId, name, description, status, created} = props.order;
    const statusColor = getOrderStatusLabelColor(status);

    return (
        <Table celled>
            <Table.Body>
                <Table.Row>
                    <Table.Cell width={2} className="table-header">
                        <FormattedMessage id="label.order-id.text" />
                    </Table.Cell>
                    <Table.Cell width={10}>{orderId}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className="table-header">
                        <FormattedMessage id="label.order-name.text" />
                    </Table.Cell>
                    <Table.Cell width={10}>{name}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className="table-header">
                        <FormattedMessage id="label.order-description.text" />
                    </Table.Cell>
                    <Table.Cell width={10}>{description}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className="table-header">
                        <FormattedMessage id="label.created.text" />
                    </Table.Cell>
                    <Table.Cell width={10}>
                        <Moment>{created}</Moment>
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2} className="table-header">
                        <FormattedMessage id="label.order-status.text" />
                    </Table.Cell>
                    <Table.Cell width={10}>
                        <Label color={statusColor}>
                            <FormattedMessage id={`enum.order-status.${status}`} />
                        </Label>
                    </Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
};

export { ShowOrderComponent as ShowOrder };