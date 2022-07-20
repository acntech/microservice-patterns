import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage} from 'react-intl';
import {Button, Icon, Label, Segment, Table} from 'semantic-ui-react';
import {getOrderStatusLabelColor} from "../core/utils";
import {Order} from "../types";

interface FragmentProps {
    orders: Order[];
    onTableRowClick: (orderId: string) => void;
    onCreateOrderButtonClick: () => void;
}

export const ShowOrderListFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {orders, onTableRowClick, onCreateOrderButtonClick} = props;

    return (
        <Segment basic>
            <Button.Group>
                <Button primary size="tiny" onClick={onCreateOrderButtonClick}>
                    <Icon name="dolly"/><FormattedMessage id="button.new"/>
                </Button>
            </Button.Group>
            <Table celled selectable>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell width={6}>
                            <FormattedMessage id="label.order.order-id"/>
                        </Table.HeaderCell>
                        <Table.HeaderCell width={4}>
                            <FormattedMessage id="label.order.name"/>
                        </Table.HeaderCell>
                        <Table.HeaderCell width={10}>
                            <FormattedMessage id="label.order.description"/>
                        </Table.HeaderCell>
                        <Table.HeaderCell width={4}>
                            <FormattedMessage id="label.order.status"/>
                        </Table.HeaderCell>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {orders.map((order, index) => {
                        const {orderId, name, description, status} = order;
                        const statusColor = getOrderStatusLabelColor(status);

                        return (
                            <Table.Row key={index} className="clickable-table-row"
                                       onClick={() => onTableRowClick(orderId)}>
                                <Table.Cell>{orderId}</Table.Cell>
                                <Table.Cell>{name}</Table.Cell>
                                <Table.Cell>{description}</Table.Cell>
                                <Table.Cell>
                                    <Label color={statusColor}>
                                        <FormattedMessage id={`enum.order-status.${status}`}/>
                                    </Label>
                                </Table.Cell>
                            </Table.Row>
                        );
                    })}
                </Table.Body>
            </Table>
        </Segment>
    );
};
