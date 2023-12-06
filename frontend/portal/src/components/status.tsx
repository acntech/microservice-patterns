import React, {FC, ReactElement} from 'react';
import {FormattedMessage} from "react-intl";
import {Order, OrderItem} from "../types";
import {Mapper} from "../core/utils";
import {Badge} from "react-bootstrap";

export interface OrderStatusBadgeProps extends React.HTMLAttributes<HTMLElement> {
    order?: Order;
}

export const OrderStatusBadge: FC<OrderStatusBadgeProps> = (props): ReactElement => {
    const {className, hidden, order} = props;

    if (!order) {
        return <></>;
    } else {
        const {status} = order;
        const statusLabelColor = Mapper.mapOrderStatusLabelColor(status);

        return (
            <Badge hidden={hidden} bg={statusLabelColor} className={className}>
                <FormattedMessage id={`enum.order-status.${status}`}/>
            </Badge>
        );
    }
};

export interface OrderItemStatusBadgeProps extends React.HTMLAttributes<HTMLElement> {
    orderItem?: OrderItem;
}

export const OrderItemStatusBadge: FC<OrderItemStatusBadgeProps> = (props): ReactElement => {
    const {className, hidden, orderItem} = props;

    if (!orderItem) {
        return <></>;
    } else {
        const {status} = orderItem;
        const statusLabelColor = Mapper.mapOrderItemStatusLabelColor(status);

        return (
            <Badge hidden={hidden} bg={statusLabelColor} className={className}>
                <FormattedMessage id={`enum.order-item-status.${status}`}/>
            </Badge>
        );
    }
};
