import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";
import Moment from "react-moment";
import Link from "next/link";
import {Button, Card, Col, ListGroup, Navbar, Row} from "react-bootstrap";
import {Order, OrderItem, OrderItemStatus, OrderStatus, Product} from "../types";
import {Mapper} from "../core/utils";
import {OrderStatusBadge, PricePanel} from "./";
import {useAppDispatch} from "../state/store";
import {deleteOrder, updateOrder} from "../state/order-slice";

interface OrderSummaryDetailsProps {
    order: Order;
    products: Product[];
}

const OrderSummaryDetails: FC<OrderSummaryDetailsProps> = (props): ReactElement => {
    const {order, products} = props;
    const {status, items} = order;

    if (status === OrderStatus.PENDING && items.length > 0) {
        const cart = Mapper.mapCart(order, products);
        const {items} = cart;
        const totalPrice = items
            .map(i => i.totalPrice)
            .reduce((totalPrice, itemPrice) => totalPrice + itemPrice, 0);
        const {currency} = items[0]; // TODO: Find better solution to this

        return (
            <>
                <FormattedMessage id="label.total-price"/>
                <PricePanel price={totalPrice} currency={currency} className="ms-2 fw-bold"/>
            </>
        );
    } else {
        return <></>;
    }
};

export interface OrderSummaryButtonsProps extends React.HTMLAttributes<HTMLElement> {
    order: Order;
}

export const OrderSummaryButtons: FC<OrderSummaryButtonsProps> = (props): ReactElement => {
    const {order} = props;
    const {orderId, status} = order;

    const dispatch = useAppDispatch();

    const onCancelButtonClicked = () => {
        dispatch(deleteOrder({orderId}));
    };

    const onConfirmButtonClicked = () => {
        dispatch(updateOrder({orderId}));
    };

    if (status === OrderStatus.PENDING) {
        return (
            <Navbar className="justify-content-end m-0 p-0">
                <Button variant="danger" onClick={onCancelButtonClicked}>
                    <FormattedMessage id="button.cancel"/>
                </Button>
                <Button variant="success" className="ms-2" onClick={onConfirmButtonClicked}>
                    <FormattedMessage id="button.confirm"/>
                </Button>
            </Navbar>
        );
    } else if (status === OrderStatus.CANCELED) {
        return (
            <Navbar className="justify-content-end m-0 p-0">
                <Button variant="primary" as={Link as any} href="/">
                    <FormattedMessage id="button.back"/>
                </Button>
            </Navbar>
        );
    } else {
        return <></>;
    }
};

export interface OrderSummaryProps extends React.HTMLAttributes<HTMLElement> {
    order?: Order;
    products: Product[];
}

export const OrderSummary: FC<OrderSummaryProps> = (props): ReactElement => {
    const {className, order, products} = props;

    if (!order) {
        return <></>;
    } else {
        return (
            <Card className={className}>
                <Card.Header>
                    <OrderStatusBadge order={order} className="float-end"/>
                </Card.Header>
                <Card.Body className="fw-light">
                    <OrderSummaryDetails order={order} products={products}/>
                </Card.Body>
                <Card.Footer>
                    <OrderSummaryButtons order={order}/>
                </Card.Footer>
            </Card>
        );
    }
};

interface OrderItemsDetailsProps {
    items: OrderItem[];
    products: Product[];
}

const OrderItemsDetails: FC<OrderItemsDetailsProps> = (props): ReactElement => {
    const {items, products} = props;

    if (items.length > 0) {
        return (
            <ListGroup>
                {
                    items
                        .filter(item => item.status === OrderItemStatus.PENDING || item.status === OrderItemStatus.RESERVED)
                        .map((item, index) => {
                            const product = products.find(product => product.productId === item.productId);
                            const {code} = product || {code: "UNKNOWN"};

                            return (
                                <ListGroup.Item key={`order-item-${index + 1}`}>
                                    <span><FormattedMessage id={`enum.product.${code}.name`}/></span>
                                    <span className="float-end">{item.quantity}</span>
                                </ListGroup.Item>
                            );
                        })
                }
            </ListGroup>
        );
    } else {
        return (
            <i>
                <FormattedMessage id="label.empty-cart"/>
            </i>
        );
    }
};

export interface OrderDetailsProps {
    order: Order | undefined;
    products: Product[];
}

export const OrderDetails: FC<OrderDetailsProps> = (props): ReactElement => {
    const {order, products} = props;

    if (!order) {
        return <></>;
    } else {
        const {orderId, status, items, created} = order;

        return (
            <Card>
                <Card.Header className="clearfix">
                    <span className="float-start">
                        <b><FormattedMessage id="title.order"/></b>
                    </span>
                    <OrderStatusBadge order={order} className="float-end"/>
                </Card.Header>
                <Card.Body>
                    <OrderItemsDetails items={items} products={products}/>
                </Card.Body>
                <Card.Footer>
                    <small>
                        <Moment format="hh:mm DD-MM-YYYY">{created}</Moment>
                    </small>
                    <Button variant="primary" className="float-end" as={Link as any} href={"/cart"}>
                        <FormattedMessage id="button.cart"/>
                    </Button>
                </Card.Footer>
            </Card>
        );
    }
};

export interface OrderListProps {
    orders: Order[];
    products: Product[];
}

export const OrderList: FC<OrderListProps> = (props): ReactElement => {
    const {orders, products} = props;

    return (
        <>
            {
                orders
                    .map((order, index) => {
                        return (
                            <Row key={`order-${index}`}>
                                <Col className="mb-4">
                                    <OrderDetails order={order} products={products}/>
                                </Col>
                            </Row>
                        )
                    })
            }
        </>
    );
};
