import React, {FC, ReactElement, useContext, useEffect, useState} from 'react';
import {Button, Card, Col, Row} from "react-bootstrap";
import {FormattedMessage} from "react-intl";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBasketShopping} from "@fortawesome/free-solid-svg-icons";
import {Price} from "./price";
import {Order, OrderItem, Product} from "../types";
import {Quantity} from "./quantity";
import {Stock} from "./stock";
import {AmountSelector} from "./amount";
import moment from "moment";
import Link from "next/link";
import {Session} from "../providers";
import {useAppDispatch} from "../state/store";
import {createOrder, createOrderItem, updateOrderItem} from "../state/order-slice";

interface ProductAttributesProps {
    detailed?: boolean;
    product: Product;
}

const ProductAttributes: FC<ProductAttributesProps> = (props: ProductAttributesProps): ReactElement => {
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
                    <Quantity packaging={packaging} quantity={quantity} measure={measure}/>
                </div>
                <div className="mt-2 fw-light">
                    <Stock stock={stock}/>
                </div>
                <div className="mt-2">
                    <Price price={price} currency={currency}/>
                </div>
            </>
        );
    } else {
        return (
            <div className="mt-2">
                <Price price={price} currency={currency}/>
            </div>
        );
    }
};

export interface ProductDetailsProps {
    detailed?: boolean;
    order?: Order;
    product: Product;
}

export const ProductDetails: FC<ProductDetailsProps> = (props: ProductDetailsProps): ReactElement => {
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
    const [productSelected, setProductSelected] = useState<boolean>(false);
    const [productChanged, setProductChanged] = useState<boolean>(false);
    const [addOrUpdate, setAddOrUpdate] = useState<boolean>(false);

    useEffect(() => {
        if (!!order) {
            const item = order.items.find((i) => i.productId === productId);
            if (!!item) {
                setOrderItem(item);
            }
        }
    }, [productId, order]);

    useEffect(() => {
        if (!!orderItem) {
            setAmount(orderItem.quantity);
        }
    }, [orderItem]);

    useEffect(() => {
        if (!!orderItem) {
            setProductSelected(true);
            if (amount !== orderItem.quantity) {
                setProductChanged(true);
            } else {
                setProductChanged(false);
            }
        }
    }, [amount, orderItem]);

    useEffect(() => {
        if (addOrUpdate) {
            if (!order) {
                const timestamp = moment().unix();
                const body = {customerId: userContext.uid, name: `order-${timestamp}`};
                dispatch(createOrder({body}));
            } else {
                const {orderId} = order;
                const body = {productId, quantity: amount};
                if (!orderItem) {
                    dispatch(createOrderItem({orderId, body}));
                } else {
                    const {itemId} = orderItem;
                    dispatch(updateOrderItem({itemId, body}));
                }
                setAddOrUpdate(false);
            }
        }
    }, [addOrUpdate, order, orderItem]);

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

    const productBorder = productSelected ? "primary" : undefined;
    const buttonTextId = productSelected ? "button.update" : "button.add";

    return (
        <Card key={productId} border={productBorder}>
            <Card.Header>
                <FontAwesomeIcon icon={faBasketShopping} size="5x"/>
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
                <AmountSelector amount={amount} onIncrease={onIncreaseAmount} onDecrease={onDecreaseAmount}/>
                <Button variant="primary" className="float-end" hidden={productChanged}
                        onClick={() => setAddOrUpdate(true)}>
                    <FormattedMessage id={buttonTextId}/>
                </Button>
            </Card.Footer>
        </Card>
    );
};

export interface ProductInventoryProps {
    order?: Order;
    products: Product[];
    columnCount: number;
}

export const ProductInventory: FC<ProductInventoryProps> = (props: ProductInventoryProps): ReactElement => {
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
                <Row key={`product-row-${i}`} className="mb-5">
                    {productRow}
                </Row>
            );
            productRow = []
        }
    }

    return (<>{productGrid}</>);
};
