import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, Container, Icon, Label, Segment, Table} from 'semantic-ui-react';
import {PrimaryHeader, SecondaryHeader} from '../../components';
import {getOrderStatusLabelColor} from '../../core/utils';

import {Order} from '../../models';

interface ComponentProps {
    orders: Order[];
    onTableRowClick: (orderId: string) => void;
    onCreateOrderButtonClick: () => void;
}

class ShowOrderListContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {orders, onTableRowClick, onCreateOrderButtonClick} = this.props;

        return (
            <Container>
                <PrimaryHeader/>
                <SecondaryHeader/>
                <Segment basic>
                    <Button.Group>
                        <Button primary size='tiny' onClick={onCreateOrderButtonClick}>
                            <Icon name='dolly'/>New Order
                        </Button>
                    </Button.Group>
                    <Table celled selectable>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell width={6}>Order ID</Table.HeaderCell>
                                <Table.HeaderCell width={4}>Name</Table.HeaderCell>
                                <Table.HeaderCell width={10}>Description</Table.HeaderCell>
                                <Table.HeaderCell width={4}>Status</Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {orders.map((order, index) => {
                                const {orderId, name, description, status} = order;
                                const statusColor = getOrderStatusLabelColor(status);

                                return (
                                    <Table.Row key={index} className='clickable-table-row'
                                               onClick={() => onTableRowClick(orderId)}>
                                        <Table.Cell collapsing={false}>{orderId}</Table.Cell>
                                        <Table.Cell>{name}</Table.Cell>
                                        <Table.Cell>{description}</Table.Cell>
                                        <Table.Cell><Label color={statusColor}>{status}</Label></Table.Cell>
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

export {ShowOrderListContainer as ShowOrderList};