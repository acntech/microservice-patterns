import {FC, ReactElement, useEffect, useState} from "react";
import {useRouter} from "next/router";
import {ClientError, ErrorPayload, Order, OrderItem, OrderItemStatus, PageState, Product} from "../../../../types";
import {ErrorPanelFragment, LoadingIndicatorFragment, mapErrorPayload} from "../../../../fragments";
import {RestClient} from "../../../../core/client";
import {Button, Icon, Label, Menu, Segment, Table} from "semantic-ui-react";
import {FormattedMessage} from "react-intl";
import {getOrderItemStatusLabelColor} from "../../../../core/utils";

const OrderItemPage: FC = (): ReactElement => {

    const router = useRouter();
    const [pageState, setPageState] = useState<PageState<any>>({status: 'LOADING'});
    const [getOrderState, setGetOrderState] = useState<PageState<Order>>({status: 'LOADING'});
    const [getProductState, setGetProductState] = useState<PageState<Product>>({status: 'PENDING'});
    const [deleteOrderItemState, setDeleteOrderItemState] = useState<PageState<Order>>({status: 'PENDING'});
    const [orderItem, setOrderItem] = useState<OrderItem>();
    const {orderId: orderIdParam, itemId: itemIdParam} = router.query
    const orderId = !orderIdParam ? undefined : typeof orderIdParam === 'string' ? orderIdParam : orderIdParam.length > 0 ? orderIdParam[0] : undefined;
    const itemId = !itemIdParam ? undefined : typeof itemIdParam === 'string' ? itemIdParam : itemIdParam.length > 0 ? itemIdParam[0] : undefined;

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

    const getProduct = (productId: string) => {
        setGetProductState({status: 'LOADING', data: undefined});
        RestClient.GET<Product>(`/api/products/${productId}`)
            .then(response => {
                setGetProductState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetProductState({status: 'FAILED', error: error.response?.body});
            });
    };

    const deleteOrderItem = (itemId: string) => {
        setDeleteOrderItemState({status: 'LOADING', data: undefined});
        RestClient.DELETE<Order>(`/api/items/${itemId}`)
            .then(response => {
                setDeleteOrderItemState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setDeleteOrderItemState({status: 'FAILED', error: error.response?.body});
            });
    };

    useEffect(() => {
        if (!!orderId) {
            getOrder(orderId);
        }
    }, []);

    useEffect(() => {
        if (getOrderState.status === 'SUCCESS' && !!getOrderState.data) {
            const orderItem = getOrderState.data.items.find(item => item.itemId === itemId);
            if (!!orderItem) {
                getProduct(orderItem.productId);
                setOrderItem(orderItem);
            }
        }
    }, [getOrderState]);

    useEffect(() => {
        if (deleteOrderItemState.status === 'SUCCESS') {
            router.push(`/orders/${orderId}`);
        }
    }, [deleteOrderItemState]);

    useEffect(() => {
        if ((getOrderState.status === 'FAILED' || getProductState.status === 'FAILED') && pageState.status !== 'FAILED') {
            const error = getOrderState.error || getProductState.error;
            setPageState({status: 'FAILED', error});
        } else if (getOrderState.status === 'SUCCESS' && getProductState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [getOrderState, getProductState]);

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
        return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.COMMON.PAGE_PARAM_MISSING'}/>
    } else if (pageState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (pageState.status === 'FAILED') {
        const {errorId, errorCode} = mapErrorPayload(pageState.error);
        return <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
    } else if (pageState.status === 'SUCCESS') {
        if (!getOrderState.data) {
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.ORDERS.ORDER_NOT_FOUND'}/>
        } else if (!getProductState.data) {
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.PRODUCTS.PRODUCTS_NOT_FOUND'}/>
        } else if (!orderItem) {
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.ORDERS.ORDER_ITEM_NOT_FOUND'}/>
        } else {
            const {name, description, price, currency} = getProductState.data;
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