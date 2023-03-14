import {Button, Icon, Label, Menu, Segment, Table} from "semantic-ui-react";
import {FormattedMessage} from "react-intl";
import {FC, ReactElement, useEffect, useReducer} from "react";
import {useRouter} from "next/router";
import {ClientError, ClientResponse, ErrorPayload, Order} from "../types";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../fragments";
import {getOrderStatusLabelColor} from "../core/utils";
import {RestConsumer} from "../core/consumer";
import {orderListReducer} from "../state/reducers";

const HomePage: FC = (): ReactElement => {
    const router = useRouter();
    const [orderListState, orderListDispatch] = useReducer(orderListReducer, {status: 'LOADING'});

    useEffect(() => {
        RestConsumer.getOrders(
            (response: ClientResponse<Order[]>) => orderListDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => orderListDispatch({status: 'FAILED', error: error.response}));
    }, []);

    if (orderListState.status === 'LOADING') {
        return (
            <LoadingIndicatorFragment/>
        );
    } else if (orderListState.status === 'FAILED') {
        return (
            <ErrorPanelFragment error={orderListState.error}/>
        );
    } else {
        const orders = orderListState.data || [];
        return (
            <Segment basic>
                <Menu>
                    <Menu.Menu position="right">
                        <Menu.Item>
                            <Button primary size="tiny" onClick={() => router.push(`/create`)}>
                                <Icon name="dolly"/><FormattedMessage id="button.new"/>
                            </Button>
                        </Menu.Item>
                    </Menu.Menu>
                </Menu>

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
                        {
                            orders.map((order, index) => {
                                const {orderId, name, description, status} = order;
                                const statusColor = getOrderStatusLabelColor(status);

                                return (
                                    <Table.Row key={index} className="clickable-table-row"
                                               onClick={() => router.push(`/orders/${orderId}`)}>
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
                            })
                        }
                    </Table.Body>
                </Table>
            </Segment>
        );
    }
};

export default HomePage;
