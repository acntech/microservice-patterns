import {FormattedMessage} from "react-intl";
import React, {FC, ReactElement, useEffect} from "react";
import {useRouter} from "next/router";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {Order, OrderItemStatus, Product, ProductListDataMissingStateError, Status} from "../../types";
import {ErrorPage, LoadingPage, OrderSummary, PageTitle, ProductInventory} from "../../components";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {getOrder, orderSelector} from "../../state/order-slice";
import {findProducts, productListSelector} from "../../state/product-list-slice";
import Link from "next/link";

interface CartContentProps {
    order?: Order;
    products: Product[]
}

const CartContent: FC<CartContentProps> = (props): ReactElement => {
    const {order, products} = props;
    const {items} = order || {items: []};
    const activeItems = items
        .filter(item => item.status !== OrderItemStatus.CANCELED);

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

    const router = useRouter();
    const dispatch = useAppDispatch();
    const orderState = useAppSelector(orderSelector);
    const productListState = useAppSelector(productListSelector);
    const {orderId: orderIdParam} = router.query;
    const orderId = Array.isArray(orderIdParam) ? orderIdParam[0] : orderIdParam;

    useEffect(() => {
        if (!!orderId) {
            dispatch(getOrder({orderId}));
        }
    }, [dispatch, orderId]);

    useEffect(() => {
        dispatch(findProducts());
    }, [dispatch]);

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
                        <Breadcrumb.Item linkAs={Link} href={`/cart/${orderId}`} active>
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
