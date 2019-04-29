import * as React from 'react';
import {Component, FunctionComponent, ReactNode} from 'react';
import Moment from 'react-moment';
import {Button, ButtonGroup, Icon, Label, Segment, Table} from 'semantic-ui-react';
import {getOrderStatusLabelColor} from '../../core/utils';

import {Order, ProductState} from '../../models';
import {ShowItemList} from "./show-item-list";

interface ComponentProps {
    order: Order;
    productState: ProductState;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
    onRefreshOrderButtonClick: () => void;
    onFetchProducts: () => void;
}

class ShowOrderContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {order, productState, onBackButtonClick, onCreateItemButtonClick, onRefreshOrderButtonClick, onFetchProducts} = this.props;

        return (
            <Segment basic>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left'/>Back</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button primary size='tiny' onClick={onCreateItemButtonClick}><Icon name='dolly'/>New Item</Button>
                </ButtonGroup>
                <ButtonGroup>
                    <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}><Icon
                        name='redo'/>Refresh</Button>
                </ButtonGroup>

                <OrderFragment order={order}/>

                <ShowItemList order={order} productState={productState} onFetchProducts={onFetchProducts}/>
            </Segment>
        );
    }
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
                    <Table.Cell width={2}><b>Order ID</b></Table.Cell>
                    <Table.Cell width={10}>{orderId}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Name</b></Table.Cell>
                    <Table.Cell width={10}>{name}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Description</b></Table.Cell>
                    <Table.Cell width={10}>{description}</Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Created</b></Table.Cell>
                    <Table.Cell width={10}>
                        <Moment>{created}</Moment>
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell width={2}><b>Status</b></Table.Cell>
                    <Table.Cell width={10}>
                        <Label color={statusColor}>{status}</Label>
                    </Table.Cell>
                </Table.Row>
            </Table.Body>
        </Table>
    );
};

export {ShowOrderContainer as ShowOrder};