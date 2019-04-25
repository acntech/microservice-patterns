import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, ButtonGroup, Container, Icon, Label, Segment, Table} from 'semantic-ui-react';
import {PrimaryHeader, SecondaryHeader} from '../../components';
import {getItemStatusLabelColor, getOrderStatusLabelColor} from '../../core/utils';

import {Order} from '../../models';

interface ComponentProps {
    order: Order;
    onBackButtonClick: () => void;
    onCreateItemButtonClick: () => void;
}

class ShowOrderContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {order, onBackButtonClick, onCreateItemButtonClick} = this.props;
        const {orderId, name, description, status: orderStatus, items} = order;
        const orderStatusColor = getOrderStatusLabelColor(orderStatus);

        return (
            <Container>
                <PrimaryHeader/>
                <SecondaryHeader/>
                <Segment basic>
                    <ButtonGroup>
                        <Button secondary size='tiny' onClick={onBackButtonClick}><Icon name='arrow left'/>Back</Button>
                    </ButtonGroup>
                    <ButtonGroup>
                        <Button primary size='tiny' onClick={onCreateItemButtonClick}><Icon name='dolly'/>New
                            Item</Button>
                    </ButtonGroup>
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
                                <Table.Cell width={2}><b>Status</b></Table.Cell>
                                <Table.Cell width={10}><Label
                                    color={orderStatusColor}>{orderStatus}</Label></Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    </Table>
                    <Table celled>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell width={6}>Product ID</Table.HeaderCell>
                                <Table.HeaderCell width={8}>Product Name</Table.HeaderCell>
                                <Table.HeaderCell width={4}>Quantity</Table.HeaderCell>
                                <Table.HeaderCell width={4}>Status</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {items.map((item, index) => {
                                const {productId, quantity, status: itemStatus} = item;
                                const itemStatusColor = getItemStatusLabelColor(itemStatus);

                                return (
                                    <Table.Row key={index}>
                                        <Table.Cell>{productId}</Table.Cell>
                                        <Table.Cell>N/A</Table.Cell>
                                        <Table.Cell>{quantity}</Table.Cell>
                                        <Table.Cell><Label color={itemStatusColor}>{itemStatus}</Label></Table.Cell>
                                    </Table.Row>
                                );
                            })}
                        </Table.Body>
                    </Table>
                </Segment>
            </Container>
        );
    }
}

export {ShowOrderContainer as ShowOrder};