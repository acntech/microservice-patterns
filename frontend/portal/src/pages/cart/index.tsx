import React, {FC, ReactElement, useContext, useEffect, useState} from "react";
import {FormattedMessage} from "react-intl";
import Link from "next/link";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {Order, OrderItemStatus, OrderStatus, Product, ProductListDataMissingStateError, Status} from "../../types";
import {ErrorPage, LoadingPage, OrderSummary, PageTitle, ProductInventory} from "../../components";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {orderSelector, setOrder} from "../../state/order-slice";
import {findOrders, orderListSelector} from "../../state/order-list-slice";
import {findProducts, productListSelector} from "../../state/product-list-slice";
import {Session} from "../../providers";

interface CartContentProps {
    order?: Order;
    products: Product[]
}

const CartContent: FC<CartContentProps> = (props): ReactElement => {
    const {order, products} = props;
    const {items} = order || {items: []};
    const activeItems = items
        .filter(item => item.status !== OrderItemStatus.DELETED);

    if (activeItems.length === 0) {
        return (
            <Row>
                <Col>
                    <i><FormattedMessage id="label.empty-cart"/></i>
                </Col>
            </Row>
        );
    } else {
        const reservedProductIds = activeItems
            .map(item => item.productId);
        const reservedProducts = products
            .filter(product => reservedProductIds.includes(product.productId))
        return <ProductInventory order={order} products={reservedProducts} columnCount={1}/>;
    }
};

const CartPage: FC = (): ReactElement => {

    const dispatch = useAppDispatch();
    const {userContext} = useContext(Session);
    const orderState = useAppSelector(orderSelector);
    const orderListState = useAppSelector(orderListSelector);
    const productListState = useAppSelector(productListSelector);
    const [openOrders, setOpenOrders] = useState<Order[]>([]);
    const {uid: customerId} = userContext;

    useEffect(() => {
        if (orderState.status === Status.SUCCESS && !orderState.data) {
            dispatch(findOrders({customerId}));
        }
    }, [dispatch, customerId, orderState]);

    useEffect(() => {
        dispatch(findProducts());
    }, [dispatch]);

    useEffect(() => {
        if (orderListState.status === Status.SUCCESS) {
            const {data: orders} = orderListState;
            const filteredOpenOrders = orders?.filter(order => order.status === OrderStatus.OPEN) || [];
            setOpenOrders(filteredOpenOrders);
        }
    }, [dispatch, orderListState]);

    useEffect(() => {
        const order = openOrders.length > 0 ? openOrders[0] : undefined;
        dispatch(setOrder({order}));
    }, [dispatch, openOrders]);

    if (orderState.status === Status.LOADING || productListState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (orderState.status === Status.FAILED) {
        return <ErrorPage error={orderState.error}/>;
    } else if (productListState.status === Status.FAILED) {
        return <ErrorPage error={productListState.error}/>;
    } else {
        const {data: order} = orderState;
        const {data: products} = productListState;

        if (!products) {
            return <ErrorPage error={ProductListDataMissingStateError}/>
        } else {
            return (
                <Container as="main">
                    <Breadcrumb className="mb-3">
                        <Breadcrumb.Item linkAs={Link} href="/">
                            <FontAwesomeIcon icon={faHouse}/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item linkAs={Link} href={"/cart"} active>
                            <FormattedMessage id="title.cart"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <PageTitle id="title.cart"/>
                    <Row>
                        <Col sm={8}>
                            <CartContent order={order} products={products}/>
                        </Col>
                        <Col sm={3}>
                            <OrderSummary className="mb-2" order={order} products={products}/>
                        </Col>
                    </Row>
                </Container>
            );
        }
    }
};

export default CartPage;
