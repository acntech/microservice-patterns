import React, {FC, ReactElement, useContext, useEffect, useState} from 'react';
import {Button, Card, Col, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {PricePanel} from "./price";
import {Order, OrderItem, OrderItemStatus, Product} from "../types";
import {QuantityPanel} from "./quantity";
import {StockPanel} from "./stock";
import {AmountSelector} from "./amount";
import moment from "moment";
import Link from "next/link";
import {Session} from "../providers";
import {useAppDispatch} from "../state/store";
import {createOrderItem, createOrderThenOrderItem, deleteOrderItem, updateOrderItem} from "../state/order-slice";
import {Variant} from "react-bootstrap/types";
import {OrderItemStatusBadge} from "./status";

interface ProductAttributesProps {
    detailed?: boolean;
    product: Product;
}

const ProductAttributes: FC<ProductAttributesProps> = (props): ReactElement => {
    const {detailed, product} = props;
    const {
        stock,
        packaging,
        quantity,
        measure,
        currency,
        price
    } = product;

    if (!!detailed) {
        return (
            <>
                <div className="mt-4 fw-light">
                    <QuantityPanel packaging={packaging} quantity={quantity} measure={measure}/>
                </div>
                <div className="mt-2 fw-light">
                    <StockPanel stock={stock}/>
                </div>
                <div className="mt-2">
                    <PricePanel price={price} currency={currency} className="fw-bold"/>
                </div>
            </>
        );
    } else {
        return (
            <div className="mt-2">
                <PricePanel price={price} currency={currency} className="fw-bold"/>
            </div>
        );
    }
};

export interface ProductDetailsProps {
    detailed?: boolean;
    order?: Order;
    product: Product;
}

export const ProductDetails: FC<ProductDetailsProps> = (props): ReactElement => {
    const {detailed, order, product} = props;
    const {
        productId,
        code,
        stock
    } = product;

    const dispatch = useAppDispatch();
    const {userContext} = useContext(Session);
    const [amount, setAmount] = useState<number>(1);
    const [orderItem, setOrderItem] = useState<OrderItem>();
    const [productCardBorder, setProductCardBorder] = useState<Variant>();
    const [hideAmountSelector, setHideAmountSelector] = useState<boolean>(false);
    const [hideAddButton, setHideAddButton] = useState<boolean>(false);
    const [hideUpdateButton, setHideUpdateButton] = useState<boolean>(true);
    const [hideRemoveButton, setHideRemoveButton] = useState<boolean>(true);
    const {uid: customerId} = userContext;

    useEffect(() => {
        if (!!order) {
            const item = order.items.find(i => i.productId === productId);
            if (!!item) {
                setOrderItem(item);
            }
        }
    }, [productId, order]);

    useEffect(() => {
        if (!!orderItem) {
            const {status, quantity} = orderItem;
            setAmount(quantity);

            switch (status) {
                case OrderItemStatus.PENDING:
                    setProductCardBorder("primary");
                    setHideAmountSelector(false);
                    setHideAddButton(false);
                    setHideUpdateButton(true);
                    setHideRemoveButton(true);
                    break;
                case OrderItemStatus.RESERVED:
                    setProductCardBorder("primary");
                    setHideAmountSelector(false);
                    setHideAddButton(true);
                    setHideUpdateButton(true);
                    setHideRemoveButton(false);
                    break;
                case OrderItemStatus.REJECTED:
                case OrderItemStatus.FAILED:
                    setProductCardBorder("danger");
                    setHideAmountSelector(false);
                    setHideAddButton(true);
                    setHideUpdateButton(true);
                    setHideRemoveButton(false);
                    break;
                case OrderItemStatus.CONFIRMED:
                    setProductCardBorder("success");
                    setHideAmountSelector(true);
                    setHideAddButton(true);
                    setHideUpdateButton(true);
                    setHideRemoveButton(true);
                    break;
                case OrderItemStatus.CANCELED:
                    setProductCardBorder("warning");
                    setHideAmountSelector(true);
                    setHideAddButton(true);
                    setHideUpdateButton(true);
                    setHideRemoveButton(true);
                    break;
            }
        }
    }, [orderItem]);

    useEffect(() => {
        if (!!orderItem) {
            const {status, quantity} = orderItem;
            if (status === OrderItemStatus.PENDING || status === OrderItemStatus.RESERVED || status === OrderItemStatus.REJECTED) {
                setHideUpdateButton(amount === quantity);
            }
        }
    }, [amount, orderItem]);

    const onIncreaseAmount = () => {
        if (amount < stock) {
            setAmount(amount + 1);
        }
    };

    const onDecreaseAmount = () => {
        if (amount > 1) {
            setAmount(amount - 1);
        }
    };

    const onAddButtonClicked = () => {
        if (!order) {
            const timestamp = moment().unix();
            const orderBody = {customerId, name: `order-${timestamp}`};
            const orderItemBody = {productId, quantity: amount};
            dispatch(createOrderThenOrderItem({orderBody, orderItemBody}));
        } else {
            const {orderId} = order;
            const body = {productId, quantity: amount};
            dispatch(createOrderItem({orderId, body}));
        }
    };

    const onUpdateButtonClicked = () => {
        if (!!orderItem) {
            const body = {productId, quantity: amount};
            const {itemId} = orderItem;
            dispatch(updateOrderItem({itemId, body}));
        }
    };

    const onRemoveButtonClicked = () => {
        if (!!orderItem) {
            const {itemId} = orderItem;
            dispatch(deleteOrderItem({itemId}));
        }
    };

    return (
        <Card key={productId} border={productCardBorder}>
            <Card.Header>
                <FontAwesomeIcon icon={faBasketShopping} size="2x"/>
                <OrderItemStatusBadge orderItem={orderItem} className="float-end"/>
            </Card.Header>
            <Card.Body>
                <Card.Title>
                    <Link href={`/product/${productId}`}>
                        <FormattedMessage id={`enum.product.${code}.name`}/>
                    </Link>
                </Card.Title>
                <Card.Subtitle className="fw-light">
                    <FormattedMessage id={`enum.product.${code}.description`}/>
                </Card.Subtitle>
                <Card.Text as="div">
                    <ProductAttributes detailed={detailed} product={product}/>
                </Card.Text>
            </Card.Body>
            <Card.Footer>
                <AmountSelector hidden={hideAmountSelector} amount={amount} onIncrease={onIncreaseAmount}
                                onDecrease={onDecreaseAmount}/>
                <div className="float-end">
                    <Button variant="primary" hidden={hideAddButton} onClick={onAddButtonClicked}>
                        <FormattedMessage id="button.add"/>
                    </Button>
                    <Button variant="primary" hidden={hideUpdateButton}
                            onClick={onUpdateButtonClicked}>
                        <FormattedMessage id="button.update"/>
                    </Button>
                    <Button variant="danger" className="ms-2" hidden={hideRemoveButton} onClick={onRemoveButtonClicked}>
                        <FormattedMessage id="button.remove"/>
                    </Button>
                </div>
            </Card.Footer>
        </Card>
    );
};

export interface ProductInventoryProps {
    order?: Order;
    products: Product[];
    columnCount: number;
}

export const ProductInventory: FC<ProductInventoryProps> = (props): ReactElement => {
    const {order, products, columnCount} = props;

    let productGrid: ReactElement[] = [];
    let productRow: ReactElement[] = [];
    for (let i = 1; i <= products.length; i++) {
        productRow.push(
            <Col key={`product-col-${i}`}>
                <ProductDetails detailed={false} order={order} product={products[i - 1]}/>
            </Col>
        );
        if (i % columnCount === 0) {
            productGrid.push(
                <Row key={`product-row-${i}`} className="mb-4">
                    {productRow}
                </Row>
            );
            productRow = []
        }
    }

    return (<>{productGrid}</>);
};
