import {Button, Icon, Label, Menu, Segment, Table} from "semantic-ui-react";
import {FormattedMessage} from "react-intl";
import {FC, ReactElement, useEffect, useState} from "react";
import {useRouter} from "next/router";
import {ClientError, ErrorPayload, Order, PageState} from "../types";
import {RestClient} from "../core/client";
import {ErrorPanelFragment, LoadingIndicatorFragment, mapErrorPayload} from "../fragments";
import {getOrderStatusLabelColor} from "../core/utils";

const HomePage: FC = (): ReactElement => {
    const router = useRouter();
    const [pageState, setPageState] = useState<PageState<Order[]>>({status: 'LOADING'});

    useEffect(() => {
        RestClient.GET<Order[]>("/api/orders")
            .then(response => {
                setPageState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setPageState({status: 'FAILED', error: error.response?.body});
            });
    }, []);

    if (pageState.status === 'LOADING') {
        return (
            <LoadingIndicatorFragment/>
        );
    } else if (pageState.status === 'FAILED') {
        const {errorId, errorCode} = mapErrorPayload(pageState.error);
        return (
            <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
        );
    } else {
        const orders = pageState.data || [];
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
