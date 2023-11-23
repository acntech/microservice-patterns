import React, {FC, ReactElement} from 'react';
import {Badge, Button, Card, ListGroup} from "react-bootstrap";
import {EnrichedOrderItem, Order, Product} from "../types";
import {FormattedMessage} from "react-intl";
import {Mapper} from "../core/utils";
import Moment from "react-moment";

interface OrderItemsDetailsProps {
    enrichedItems: EnrichedOrderItem[];
}

const OrderItemsDetails: FC<OrderItemsDetailsProps> = (props: OrderItemsDetailsProps): ReactElement => {
    const {enrichedItems} = props;

    if (enrichedItems.length > 0) {
        return (
            <ListGroup>
                {
                    enrichedItems.map((item, index) => {
                        return (
                            <ListGroup.Item key={`order-item-${index + 1}`}>
                                <span><small>{item.name}</small></span>
                                <span className="float-end"><small>{item.quantity}</small></span>
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

export const OrderDetails: FC<OrderDetailsProps> = (props: OrderDetailsProps): ReactElement => {
    const {order, products} = props;

    if (!order) {
        return <></>;
    } else {
        const enrichedOrder = Mapper.mapEnrichOrder(order, products);
        const {orderId, status, statusColor, enrichedItems, created} = enrichedOrder;

        return (
            <Card>
                <Card.Header className="clearfix">
                    <span className="float-start">
                        <b><FormattedMessage id="title.order"/></b>
                    </span>
                    <Badge bg={statusColor} className="float-end">
                        <FormattedMessage id={`enum.order-status.${status}`}/>
                    </Badge>
                </Card.Header>
                <Card.Body>
                    <OrderItemsDetails enrichedItems={enrichedItems}/>
                </Card.Body>
                <Card.Footer>
                    <small>
                        <Moment format="hh:mm DD-MM-YYYY">{created}</Moment>
                    </small>
                    <Button variant="primary" href={`/cart/${orderId}`} className="float-end">
                        <FormattedMessage id="button.cart"/>
                    </Button>
                </Card.Footer>
            </Card>
        );
    }
}
