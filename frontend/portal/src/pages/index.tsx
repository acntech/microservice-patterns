import {FormattedMessage} from "react-intl";
import React, {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {useRouter} from "next/router";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faDolly, faRotateRight} from "@fortawesome/free-solid-svg-icons";
import {ClientError, ClientResponse, ErrorPayload, Order, Status} from "../types";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../fragments";
import {getOrderStatusLabelColor} from "../core/utils";
import {RestConsumer} from "../core/consumer";
import {orderListReducer} from "../state/reducers";
import {Badge, Button, Container, Nav, Table} from "react-bootstrap";

const HomePage: FC = (): ReactElement => {
    const router = useRouter();
    const [pageStatus, setPageStatus] = useState<Status>(Status.LOADING);
    const [orderListState, orderListDispatch] = useReducer(orderListReducer, {status: Status.LOADING});

    const findOrders = () => {
        RestConsumer.getOrders(
            (response: ClientResponse<Order[]>) => orderListDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => orderListDispatch({status: Status.FAILED, error: error.response}));
    }

    useEffect(() => {
        findOrders();
    }, []);

    useEffect(() => {
        if (orderListState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderListState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if (orderListState.status === Status.FAILED && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [pageStatus, orderListState]);

    const onRefreshOrderButtonClick = () => {
        setPageStatus(Status.LOADING);
        findOrders();
    };

    if (orderListState.status === Status.LOADING) {
        return <LoadingIndicatorFragment/>;
    } else if (orderListState.status === Status.FAILED) {
        return <ErrorPanelFragment error={orderListState.error}/>;
    } else {
        const orders = orderListState.data || [];
        return (
            <Container as="main">
                <h2 className="mb-3"><FormattedMessage id="title.order-list"/></h2>

                <Nav className="justify-content-end mb-3">
                    <Nav.Item className="me-2">
                        <Button variant="secondary" onClick={onRefreshOrderButtonClick}>
                            <FontAwesomeIcon icon={faRotateRight}/><FormattedMessage id="button.refresh"/>
                        </Button>
                    </Nav.Item>
                    <Nav.Item>
                        <Button variant="primary" onClick={() => router.push(`/create`)}>
                            <FontAwesomeIcon icon={faDolly}/><FormattedMessage id="button.new-order"/>
                        </Button>
                    </Nav.Item>
                </Nav>

                <Table bordered hover>
                    <thead>
                    <tr>
                        <th>
                            <FormattedMessage id="label.order.order-id"/>
                        </th>
                        <th>
                            <FormattedMessage id="label.order.name"/>
                        </th>
                        <th>
                            <FormattedMessage id="label.order.description"/>
                        </th>
                        <th>
                            <FormattedMessage id="label.order.status"/>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        orders.map((order, index) => {
                            const {orderId, name, description, status} = order;
                            const statusColor = getOrderStatusLabelColor(status);

                            return (
                                <tr key={index} className="clickable-table-row"
                                    onClick={() => router.push(`/orders/${orderId}`)}>
                                    <td>{orderId}</td>
                                    <td>{name}</td>
                                    <td>{description}</td>
                                    <td>
                                        <Badge bg={statusColor}>
                                            <FormattedMessage id={`enum.order-status.${status}`}/>
                                        </Badge>
                                    </td>
                                </tr>
                            );
                        })
                    }
                    </tbody>
                </Table>
            </Container>
        );
    }
};

export default HomePage;
