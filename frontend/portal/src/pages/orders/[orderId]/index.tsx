import {FC, ReactElement, useEffect, useState} from "react";
import {FormattedMessage} from "react-intl";
import Moment from "react-moment";
import {useRouter} from "next/router";
import {Button, Icon, Label, Menu, Segment, SemanticCOLORS, Table} from "semantic-ui-react";
import {ErrorPanelFragment, LoadingIndicatorFragment, mapErrorPayload} from "../../../fragments";
import {getOrderItemStatusLabelColor, getOrderStatusLabelColor} from "../../../core/utils";
import {RestClient} from "../../../core/client";
import {
    ClientError,
    ErrorPayload,
    Order,
    OrderItem,
    OrderItemStatus,
    OrderStatus,
    PageState,
    Product
} from "../../../types";

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
    return order.status === OrderStatus.PENDING &&
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
    const [pageState, setPageState] = useState<PageState<any>>({status: 'LOADING'});
    const [getOrderState, setGetOrderState] = useState<PageState<Order>>({status: 'LOADING'});
    const [getProductListState, setGetProductListState] = useState<PageState<Product[]>>({status: 'LOADING'});
    const {orderId: orderIdParam} = router.query;
    const orderId = !orderIdParam ? undefined : typeof orderIdParam === 'string' ? orderIdParam : orderIdParam.length > 0 ? orderIdParam[0] : undefined;

    const getOrder = (orderId: string) => {
        setGetOrderState({status: 'LOADING', data: undefined});
        RestClient.GET<Order>(`/api/orders/${orderId}`)
            .then(response => {
                setGetOrderState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetOrderState({status: 'FAILED', error: error.response?.body});
            });
    };

    const getProductList = () => {
        setGetProductListState({status: 'LOADING', data: undefined});
        RestClient.GET<Product[]>('/api/products')
            .then(response => {
                setGetProductListState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetProductListState({status: 'FAILED', error: error.response?.body});
            });
    };

    const confirmOrder = () => {
        setPageState({status: 'LOADING'});
        setGetOrderState({status: 'LOADING', data: undefined});
        RestClient.PUT<Order>(`/api/orders/${orderId}`)
            .then(response => {
                setGetOrderState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetOrderState({status: 'FAILED', error: error.response?.body});
            });
    };

    const deleteOrder = () => {
        setPageState({status: 'LOADING'});
        setGetOrderState({status: 'LOADING', data: undefined});
        RestClient.DELETE<Order>(`/api/orders/${orderId}`)
            .then(response => {
                setGetOrderState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetOrderState({status: 'FAILED', error: error.response?.body});
            });
    };

    useEffect(() => {
        if (!!orderId) {
            getOrder(orderId);
            getProductList();
        }
    }, []);

    useEffect(() => {
        if ((getOrderState.status === 'FAILED' || getProductListState.status === 'FAILED') && pageState.status !== 'FAILED') {
            const error = getOrderState.error || getProductListState.error;
            setPageState({status: 'FAILED', error});
        } else if (getOrderState.status === 'SUCCESS' && getProductListState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [getOrderState, getProductListState]);

    const onRefreshOrderButtonClick = () => {
        if (!!orderId) {
            setPageState({status: 'LOADING'});
            getOrder(orderId);
            getProductList();
        }
    };

    if (!orderId) {
        console.log("ID")
        return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.COMMON.PAGE_PARAM_MISSING'}/>
    } else if (pageState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>
    } else if (pageState.status === 'FAILED') {
        console.log("FAILED")
        const {errorId, errorCode} = mapErrorPayload(pageState.error);
        return <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
    } else if (getOrderState.status === 'SUCCESS') {
        if (!getOrderState.data) {
            console.log("ORDER")
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.ORDERS.ORDER_NOT_FOUND'}/>
        } else if (!getProductListState.data) {
            console.log("PRODUCTS")
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.PRODUCTS.PRODUCTS_NOT_FOUND'}/>
        } else {
            const {data: order} = getOrderState;
            const {data: products} = getProductListState;
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
                                        size="tiny" onClick={confirmOrder}>
                                    <Icon name="check"/><FormattedMessage id="button.confirm"/>
                                </Button>
                            </Menu.Item>
                            <Menu.Item>
                                <Button negative={cancelOrderButtonActive}
                                        disabled={!cancelOrderButtonActive}
                                        size="tiny" onClick={deleteOrder}>
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
