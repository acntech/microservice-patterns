import {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {useRouter} from "next/router";
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
import {Button, Icon, Label, Menu, Segment, Table} from "semantic-ui-react";
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
    }, []);

    useEffect(() => {
        if (orderState.status === Status.SUCCESS && !!orderState.data) {
            const orderItems = orderState.data.items || []
            const orderItem = orderItems.find(item => item.itemId === itemId);
            if (!!orderItem) {
                getProduct(orderItem.productId);
                setOrderItem(orderItem);
            }
        }
    }, [orderState]);

    useEffect(() => {
        if (deleteOrderState.status === Status.SUCCESS) {
            router.push(`/orders/${orderId}`);
        }
    }, [deleteOrderState]);

    useEffect(() => {
        if (orderState.status === Status.LOADING && productState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderState.status === Status.SUCCESS && productState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if ((orderState.status === Status.FAILED || productState.status === Status.FAILED) && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [orderState, productState]);

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
                <Segment basic>
                    <Menu>
                        <Menu.Menu position='left'>
                            <Menu.Item>
                                <Button secondary size="tiny" onClick={() => router.push(`/orders/${orderId}`)}>
                                    <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                                </Button>
                            </Menu.Item>
                            <Menu.Item>
                                <Button secondary size='tiny' onClick={onRefreshButtonClick}>
                                    <Icon name="redo"/><FormattedMessage id="button.refresh"/>
                                </Button>
                            </Menu.Item>
                        </Menu.Menu>
                        <Menu.Menu position='right'>
                            <Menu.Item>
                                <Button negative={deleteButtonActive}
                                        disabled={!deleteButtonActive}
                                        size="tiny" onClick={onDeleteOrderItemButtonClick}>
                                    <Icon name="delete"/><FormattedMessage id="button.delete"/>
                                </Button>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu>
                    <Table celled>
                        <Table.Body>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.product.product-id"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{productId}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.product.name"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{name}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.product.description"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{description}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order-item.status"/>
                                </Table.Cell>
                                <Table.Cell width={10}>
                                    <Label color={statusColor}>
                                        <FormattedMessage id={`enum.order-item-status.${status}`}/>
                                    </Label>
                                </Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order-item.quantity"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{quantity}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.product.unit-price"/>
                                </Table.Cell>
                                <Table.Cell width={10}>
                                    {currency} {price.toFixed(2)}
                                </Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order-item.total-price"/>
                                </Table.Cell>
                                <Table.Cell width={10}>
                                    {currency} {totalPrice.toFixed(2)}
                                </Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    </Table>
                </Segment>
            );
        }
    } else {
        return <></>;
    }
}

export default OrderItemPage;