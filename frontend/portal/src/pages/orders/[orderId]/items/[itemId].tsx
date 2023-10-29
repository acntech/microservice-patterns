import {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {useRouter} from "next/router";
import {Badge, Button, Container, Nav, Table} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faRotateRight, faXmark} from "@fortawesome/free-solid-svg-icons";
import {
    ClientError,
    ClientResponse,
    ErrorCode,
    ErrorPayload,
    Order,
    OrderItem,
    OrderItemStatus,
    Product,
    Status
} from "../../../../types";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../../../../fragments";
import {FormattedMessage} from "react-intl";
import {getOrderItemStatusLabelColor} from "../../../../core/utils";
import {RestConsumer} from "../../../../core/consumer";
import {orderReducer, productReducer} from "../../../../state/reducers";

const OrderItemPage: FC = (): ReactElement => {

    const router = useRouter();
    const [pageStatus, setPageStatus] = useState<Status>(Status.LOADING);
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: Status.LOADING});
    const [productState, productDispatch] = useReducer(productReducer, {status: Status.LOADING});
    const [deleteOrderState, deleteOrderDispatch] = useReducer(orderReducer, {status: Status.PENDING});
    const [orderItem, setOrderItem] = useState<OrderItem>();
    const {orderId: orderIdParam, itemId: itemIdParam} = router.query;
    const orderId = Array.isArray(orderIdParam) ? orderIdParam[0] : orderIdParam;
    const itemId = Array.isArray(itemIdParam) ? itemIdParam[0] : itemIdParam;

    const getOrder = (orderId: string) => {
        orderDispatch({status: Status.LOADING, data: undefined});
        RestConsumer.getOrder(orderId,
            (response: ClientResponse<Order>) => orderDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: Status.FAILED, error: error.response}));
    };

    const getProduct = (productId: string) => {
        productDispatch({status: Status.LOADING, data: undefined});
        RestConsumer.getProduct(productId,
            (response: ClientResponse<Product>) => productDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => productDispatch({status: Status.FAILED, error: error.response}));
    };

    const deleteOrderItem = (itemId: string) => {
        deleteOrderDispatch({status: Status.LOADING, data: undefined});
        RestConsumer.deleteOrderItem(itemId,
            (response: ClientResponse<Order>) => deleteOrderDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => deleteOrderDispatch({status: Status.FAILED, error: error.response}));
    };

    useEffect(() => {
        if (!!orderId) {
            getOrder(orderId);
        }
    }, [orderId]);

    useEffect(() => {
        if (orderState.status === Status.SUCCESS && !!orderState.data) {
            const orderItems = orderState.data.items || []
            const orderItem = orderItems.find(item => item.itemId === itemId);
            if (!!orderItem) {
                getProduct(orderItem.productId);
                setOrderItem(orderItem);
            }
        }
    }, [itemId, orderState]);

    useEffect(() => {
        if (!!orderId && deleteOrderState.status === Status.SUCCESS) {
            router.push(`/orders/${orderId}`);
        }
    }, [deleteOrderState, orderId]);

    useEffect(() => {
        if (orderState.status === Status.LOADING && productState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderState.status === Status.SUCCESS && productState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if ((orderState.status === Status.FAILED || productState.status === Status.FAILED) && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [pageStatus, orderState, productState]);

    const onDeleteOrderItemButtonClick = () => {
        if (!!orderId && !!itemId) {
            deleteOrderItem(itemId);
        }
    };

    const onRefreshButtonClick = () => {
        if (!!orderId) {
            getOrder(orderId);
        }
    };

    if (!orderId || !itemId) {
        return <ErrorPanelFragment error={{status: ErrorCode.PARAM_MISSING}}/>
    } else if (pageStatus === Status.LOADING) {
        return <LoadingIndicatorFragment/>;
    } else if (pageStatus === Status.FAILED) {
        return <ErrorPanelFragment error={orderState.error || productState.error}/>
    } else if (pageStatus === Status.SUCCESS) {
        if (!orderState.data) {
            return <ErrorPanelFragment error={{status: ErrorCode.ORDER_DATA_MISSING}}/>
        } else if (!productState.data) {
            return <ErrorPanelFragment error={{status: ErrorCode.PRODUCT_DATA_MISSING}}/>
        } else if (!orderItem) {
            return <ErrorPanelFragment error={{status: ErrorCode.ORDER_ITEM_DATA_MISSING}}/>
        } else {
            const {name, description, price, currency} = productState.data;
            const {productId, quantity, status} = orderItem;

            const totalPrice = price * quantity;
            const statusColor = getOrderItemStatusLabelColor(status);
            const deleteButtonActive = status === OrderItemStatus.RESERVED || status === OrderItemStatus.REJECTED;

            return (
                <Container as="main">
                    <Nav className="justify-content-start">
                        <Nav.Item>
                            <Button variant="secondary" onClick={() => router.push(`/orders/${orderId}`)}>
                                <FontAwesomeIcon icon={faArrowLeft}/><FormattedMessage id="button.back"/>
                            </Button>
                        </Nav.Item>
                        <Nav.Item>
                            <Button variant="secondary" onClick={onRefreshButtonClick}>
                                <FontAwesomeIcon icon={faRotateRight}/><FormattedMessage id="button.refresh"/>
                            </Button>
                        </Nav.Item>
                    </Nav>
                    <Nav className="justify-content-end">
                        <Nav.Item>
                            <Button variant="danger"
                                    disabled={!deleteButtonActive}
                                    onClick={onDeleteOrderItemButtonClick}>
                                <FontAwesomeIcon icon={faXmark}/><FormattedMessage id="button.delete"/>
                            </Button>
                        </Nav.Item>
                    </Nav>
                    <Table bordered>
                        <tbody>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.product.product-id"/>
                            </td>
                            <td width={10}>{productId}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.product.name"/>
                            </td>
                            <td width={10}>{name}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.product.description"/>
                            </td>
                            <td width={10}>{description}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order-item.status"/>
                            </td>
                            <td width={10}>
                                <Badge bg={statusColor}>
                                    <FormattedMessage id={`enum.order-item-status.${status}`}/>
                                </Badge>
                            </td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order-item.quantity"/>
                            </td>
                            <td width={10}>{quantity}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.product.unit-price"/>
                            </td>
                            <td width={10}>
                                {currency} {price.toFixed(2)}
                            </td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.order-item.total-price"/>
                            </td>
                            <td width={10}>
                                {currency} {totalPrice.toFixed(2)}
                            </td>
                        </tr>
                        </tbody>
                    </Table>
                </Container>
            );
        }
    } else {
        return <></>;
    }
}

export default OrderItemPage;