import {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {FormattedMessage} from "react-intl";
import Moment from "react-moment";
import {useRouter} from "next/router";
import {Button, Icon, Label, Menu, Segment, SemanticCOLORS, Table} from "semantic-ui-react";
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
    statusColor: SemanticCOLORS;
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
    const [pageStatus, setPageStatus] = useState<Status>('LOADING');
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: 'LOADING'});
    const [productListState, productListDispatch] = useReducer(productListReducer, {status: 'LOADING'});
    const {orderId: orderIdParam} = router.query;
    const orderId = !orderIdParam ? undefined : typeof orderIdParam === 'string' ? orderIdParam : orderIdParam.length > 0 ? orderIdParam[0] : undefined;

    const getOrder = (orderId: string) => {
        orderDispatch({status: 'LOADING', data: undefined});
        RestConsumer.getOrder(orderId,
            (response: ClientResponse<Order>) => orderDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: 'FAILED', error: error.response}));
    };

    const getProductList = () => {
        productListDispatch({status: 'LOADING', data: undefined});
        RestConsumer.getProducts(
            (response: ClientResponse<Product[]>) => productListDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => productListDispatch({status: 'FAILED', error: error.response}));
    };

    const confirmOrder = (orderId: string) => {
        setPageStatus('LOADING');
        orderDispatch({status: 'LOADING', data: undefined});
        RestConsumer.updateOrder(orderId,
            (response: ClientResponse<Order>) => orderDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: 'FAILED', error: error.response}));
    };

    const deleteOrder = (orderId: string) => {
        setPageStatus('LOADING');
        orderDispatch({status: 'LOADING', data: undefined});
        RestConsumer.deleteOrder(orderId,
            (response: ClientResponse<Order>) => orderDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: 'FAILED', error: error.response}));
    };

    useEffect(() => {
        if (!!orderId) {
            getOrder(orderId);
            getProductList();
        }
    }, []);

    useEffect(() => {
        if (orderState.status === 'LOADING' && productListState.status === 'LOADING' && pageStatus !== 'LOADING') {
            setPageStatus('LOADING');
        } else if (orderState.status === 'SUCCESS' && productListState.status === 'SUCCESS' && pageStatus === 'LOADING') {
            setPageStatus('SUCCESS');
        } else if ((orderState.status === 'FAILED' || productListState.status === 'FAILED') && pageStatus !== 'FAILED') {
            setPageStatus('FAILED');
        }
    }, [orderState, productListState]);

    const onRefreshOrderButtonClick = () => {
        if (!!orderId) {
            setPageStatus('LOADING');
            getOrder(orderId);
            getProductList();
        }
    };

    if (!orderId) {
        return <ErrorPanelFragment error={{status: ErrorCode.PARAM_MISSING}}/>
    } else if (pageStatus === 'LOADING') {
        return <LoadingIndicatorFragment/>
    } else if (pageStatus === 'FAILED') {
        return <ErrorPanelFragment error={orderState.error || productListState.error}/>
    } else if (orderState.status === 'SUCCESS') {
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
                <Segment basic>
                    <Menu>
                        <Menu.Menu position='left'>
                            <Menu.Item>
                                <Button secondary size="tiny" onClick={() => router.push('/')}>
                                    <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                                </Button>
                            </Menu.Item>
                            <Menu.Item>
                                <Button secondary size='tiny' onClick={onRefreshOrderButtonClick}>
                                    <Icon name="redo"/><FormattedMessage id="button.refresh"/>
                                </Button>
                            </Menu.Item>
                        </Menu.Menu>
                        <Menu.Menu position='right'>
                            <Menu.Item>
                                <Button positive={confirmOrderButtonActive}
                                        disabled={!confirmOrderButtonActive}
                                        size="tiny" onClick={() => confirmOrder(orderId)}>
                                    <Icon name="check"/><FormattedMessage id="button.confirm"/>
                                </Button>
                            </Menu.Item>
                            <Menu.Item>
                                <Button negative={cancelOrderButtonActive}
                                        disabled={!cancelOrderButtonActive}
                                        size="tiny" onClick={() => deleteOrder(orderId)}>
                                    <Icon name="delete"/><FormattedMessage id="button.cancel"/>
                                </Button>
                            </Menu.Item>
                            <Menu.Item>
                                <Button primary={createOrderItemButtonActive}
                                        disabled={!createOrderItemButtonActive}
                                        size="tiny" onClick={() => router.push(`/orders/${orderId}/create`)}>
                                    <Icon name="dolly"/><FormattedMessage id="button.new-item"/>
                                </Button>
                            </Menu.Item>
                        </Menu.Menu>
                    </Menu>

                    <Table celled>
                        <Table.Body>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order.order-id"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{orderId}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order.name"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{name}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order.description"/>
                                </Table.Cell>
                                <Table.Cell width={10}>{description}</Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order.created-timestamp"/>
                                </Table.Cell>
                                <Table.Cell width={10}>
                                    <Moment format='YYYY-MM-DD hh:mm:ss'>{created}</Moment>
                                </Table.Cell>
                            </Table.Row>
                            <Table.Row>
                                <Table.Cell width={2} className="table-header">
                                    <FormattedMessage id="label.order.status"/>
                                </Table.Cell>
                                <Table.Cell width={10}>
                                    <Label color={statusColor}>
                                        <FormattedMessage id={`enum.order-status.${status}`}/>
                                    </Label>
                                </Table.Cell>
                            </Table.Row>
                        </Table.Body>
                    </Table>

                    <Table celled selectable>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell width={6}><FormattedMessage
                                    id="label.order-item.product-id"/></Table.HeaderCell>
                                <Table.HeaderCell width={6}><FormattedMessage
                                    id="label.order-item.name"/></Table.HeaderCell>
                                <Table.HeaderCell width={2}><FormattedMessage
                                    id="label.order-item.quantity"/></Table.HeaderCell>
                                <Table.HeaderCell width={8}><FormattedMessage
                                    id="label.order-item.status"/></Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
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
                                        <Table.Row key={index} className="clickable-table-row"
                                                   onClick={() => router.push(`/orders/${orderId}/items/${itemId}`)}>
                                            <Table.Cell>{productId}</Table.Cell>
                                            <Table.Cell>{name || 'N/A'}</Table.Cell>
                                            <Table.Cell>{quantity}</Table.Cell>
                                            <Table.Cell>
                                                <Label color={statusColor}>
                                                    <FormattedMessage id={`enum.order-item-status.${status}`}/>
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
    } else {
        return <></>;
    }
};

export default OrderPage;
