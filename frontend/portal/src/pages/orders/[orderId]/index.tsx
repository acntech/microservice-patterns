import React, {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {FormattedMessage} from "react-intl";
import Moment from "react-moment";
import {useRouter} from "next/router";
import {Badge, Button, Container, Nav, Table} from "react-bootstrap";
import {Variant} from "react-bootstrap/types";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faCheck, faDolly, faRotateRight, faXmark} from "@fortawesome/free-solid-svg-icons";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../../../fragments";
import {getOrderItemStatusLabelColor, getOrderStatusLabelColor} from "../../../core/utils";
import {
    ClientError,
    ClientResponse,
    ErrorCode,
    ErrorPayload,
    Order,
    OrderItem,
    OrderItemStatus,
    OrderStatus,
    Product,
    Status
} from "../../../types";
import {RestConsumer} from "../../../core/consumer";
import {orderReducer, productListReducer} from "../../../state/reducers";

interface ViewOrderItem {
    itemId: string;
    productId: string;
    name?: string;
    quantity: number;
    status: OrderItemStatus;
    statusColor: Variant;
}

const isCreateOrderItemButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING;
};

const isConfirmOrderButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING && order.items.length > 0 &&
        order.items
            .map(item => item.status)
            .filter(status => status !== OrderItemStatus.RESERVED && status !== OrderItemStatus.CANCELED).length === 0;
};

const isCancelOrderButtonActive = (order: Order): boolean => {
    return order.status === OrderStatus.PENDING;
};

const findProductName = (productId: string, products: Product[]): string | undefined => {
    return products
        .filter(product => product.productId === productId)
        .map(product => product.name)
        .pop();
};

const enrichOrderItem = (orderItem: OrderItem, products: Product[]): ViewOrderItem => {
    return {
        itemId: orderItem.itemId,
        productId: orderItem.productId,
        name: findProductName(orderItem.productId, products),
        quantity: orderItem.quantity,
        status: orderItem.status,
        statusColor: getOrderItemStatusLabelColor(orderItem.status)
    }
};

const OrderPage: FC = (): ReactElement => {

    const router = useRouter();
    const [pageStatus, setPageStatus] = useState<Status>(Status.LOADING);
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: Status.LOADING});
    const [productListState, productListDispatch] = useReducer(productListReducer, {status: Status.LOADING});
    const {orderId: orderIdParam} = router.query;
    const orderId = Array.isArray(orderIdParam) ? orderIdParam[0] : orderIdParam;

    const getOrder = (orderId: string) => {
        orderDispatch({status: Status.LOADING, data: undefined});
        RestConsumer.getOrder(orderId,
            (response: ClientResponse<Order>) => orderDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: Status.FAILED, error: error.response}));
    };

    const getProductList = () => {
        productListDispatch({status: Status.LOADING, data: undefined});
        RestConsumer.getProducts(
            (response: ClientResponse<Product[]>) => productListDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => productListDispatch({status: Status.FAILED, error: error.response}));
    };

    const onConfirmOrderButtonClick = () => {
        if (!!orderId) {
            setPageStatus(Status.LOADING);
            orderDispatch({status: Status.LOADING, data: undefined});
            RestConsumer.updateOrder(orderId,
                (response: ClientResponse<Order>) => orderDispatch({status: Status.SUCCESS, data: response}),
                (error: ClientError<ErrorPayload>) => orderDispatch({status: Status.FAILED, error: error.response}));
        }
    };

    const onDeleteOrderButtonClick = () => {
        if (!!orderId) {
            setPageStatus(Status.LOADING);
            orderDispatch({status: Status.LOADING, data: undefined});
            RestConsumer.deleteOrder(orderId,
                (response: ClientResponse<Order>) => orderDispatch({status: Status.SUCCESS, data: response}),
                (error: ClientError<ErrorPayload>) => orderDispatch({status: Status.FAILED, error: error.response}));
        }
    };

    const onRefreshOrderButtonClick = () => {
        if (!!orderId) {
            setPageStatus(Status.LOADING);
            getOrder(orderId);
            getProductList();
        }
    };

    useEffect(() => {
        if (!!orderId) {
            getOrder(orderId);
            getProductList();
        }
    }, [orderId]);

    useEffect(() => {
        if (orderState.status === Status.LOADING && productListState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderState.status === Status.SUCCESS && productListState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if ((orderState.status === Status.FAILED || productListState.status === Status.FAILED) && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [pageStatus, orderState, productListState]);

    if (!orderId) {
        return <ErrorPanelFragment error={{status: ErrorCode.PARAM_MISSING}}/>
    } else if (pageStatus === Status.LOADING) {
        return <LoadingIndicatorFragment/>
    } else if (pageStatus === Status.FAILED) {
        return <ErrorPanelFragment error={orderState.error || productListState.error}/>
    } else if (orderState.status === Status.SUCCESS) {
        if (!orderState.data) {
            return <ErrorPanelFragment error={{status: ErrorCode.ORDER_DATA_MISSING}}/>
        } else if (!productListState.data) {
            return <ErrorPanelFragment error={{status: ErrorCode.PRODUCT_LIST_DATA_MISSING}}/>
        } else {
            const {data: order} = orderState;
            const {data: products} = productListState;
            const {orderId, name, description, status, created, items} = order;

            const statusColor = getOrderStatusLabelColor(status);
            const createOrderItemButtonActive = isCreateOrderItemButtonActive(order);
            const confirmOrderButtonActive = isConfirmOrderButtonActive(order);
            const cancelOrderButtonActive = isCancelOrderButtonActive(order);
            const viewOrderItemList = items.map(orderItem => enrichOrderItem(orderItem, products));

            return (
                <Container as="main">
                    <h2 className="mb-3"><FormattedMessage id="title.order"/></h2>

                    <Nav className="justify-content-end mb-3">
                        <Nav.Item className="me-2">
                            <Button variant="secondary" onClick={() => router.push('/')}>
                                <FontAwesomeIcon icon={faArrowLeft}/><FormattedMessage id="button.back"/>
                            </Button>
                        </Nav.Item>
                        <Nav.Item className="me-2">
                            <Button variant="secondary" onClick={onRefreshOrderButtonClick}>
                                <FontAwesomeIcon icon={faRotateRight}/><FormattedMessage id="button.refresh"/>
                            </Button>
                        </Nav.Item>
                        <Nav.Item className="me-2">
                            <Button variant="danger"
                                    disabled={!cancelOrderButtonActive}
                                    onClick={onDeleteOrderButtonClick}>
                                <FontAwesomeIcon icon={faXmark}/><FormattedMessage id="button.cancel"/>
                            </Button>
                        </Nav.Item>
                        <Nav.Item>
                            <Button variant="success"
                                    disabled={!confirmOrderButtonActive}
                                    onClick={onConfirmOrderButtonClick}>
                                <FontAwesomeIcon icon={faCheck}/><FormattedMessage id="button.confirm"/>
                            </Button>
                        </Nav.Item>
                    </Nav>

                    <Table bordered>
                        <tbody>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order.order-id"/>
                            </td>
                            <td width={10}>{orderId}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order.name"/>
                            </td>
                            <td width={10}>{name}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order.description"/>
                            </td>
                            <td width={10}>{description}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order.created-timestamp"/>
                            </td>
                            <td width={10}>
                                <Moment format='YYYY-MM-DD hh:mm:ss'>{created}</Moment>
                            </td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order.status"/>
                            </td>
                            <td width={10}>
                                <Badge bg={statusColor}>
                                    <FormattedMessage id={`enum.order-status.${status}`}/>
                                </Badge>
                            </td>
                        </tr>
                        </tbody>
                    </Table>

                    <h3 className="mb-3"><FormattedMessage id="title.order-items"/></h3>
                    <Nav className="justify-content-end mb-3">
                        <Nav.Item>
                            <Button variant="primary"
                                    disabled={!createOrderItemButtonActive}
                                    onClick={() => router.push(`/orders/${orderId}/create`)}>
                                <FontAwesomeIcon icon={faDolly}/><FormattedMessage id="button.new-item"/>
                            </Button>
                        </Nav.Item>
                    </Nav>
                    <Table bordered hover>
                        <thead>
                        <tr>
                            <th><FormattedMessage
                                id="label.order-item.product-id"/></th>
                            <th><FormattedMessage
                                id="label.order-item.name"/></th>
                            <th><FormattedMessage
                                id="label.order-item.quantity"/></th>
                            <th><FormattedMessage
                                id="label.order-item.status"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        {
                            viewOrderItemList.map((viewOrderItem, index) => {
                                const {
                                    itemId,
                                    productId,
                                    name,
                                    quantity,
                                    status,
                                    statusColor
                                } = viewOrderItem;

                                return (
                                    <tr key={index} className="clickable-table-row"
                                        onClick={() => router.push(`/orders/${orderId}/items/${itemId}`)}>
                                        <td>{productId}</td>
                                        <td>{name || 'N/A'}</td>
                                        <td>{quantity}</td>
                                        <td>
                                            <Badge bg={statusColor}>
                                                <FormattedMessage id={`enum.order-item-status.${status}`}/>
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
    } else {
        return <></>;
    }
};

export default OrderPage;
