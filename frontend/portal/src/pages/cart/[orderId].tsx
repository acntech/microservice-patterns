import {FormattedMessage} from "react-intl";
import React, {FC, ReactElement, useEffect} from "react";
import {useRouter} from "next/router";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {ProductListDataMissingError, Status} from "../../types";
import {ErrorPage, LoadingPage, PageTitle, ProductInventory} from "../../components";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {getOrder, orderSelector} from "../../state/order-slice";
import {findProducts, productListSelector} from "../../state/product-list-slice";

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
            return <ErrorPage error={ProductListDataMissingError}/>
        } else if (!order || !order.items || order.items.length === 0) {
            return (
                <Container as="main">
                    <Breadcrumb className="mb-3">
                        <Breadcrumb.Item href="/">
                            <FontAwesomeIcon icon={faHouse}/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item href={`/cart/${orderId}`} active>
                            <FormattedMessage id="title.cart"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <PageTitle id="title.cart"/>
                    <Row>
                        <Col>
                            <i>
                                <FormattedMessage id="label.empty-cart"/>
                            </i>
                        </Col>
                    </Row>
                </Container>
            );
        } else {
            const reservedProductIds = order.items.map(item => item.productId);
            const reservedProducts = products.filter(product => reservedProductIds.includes(product.productId))

            return (
                <Container as="main">
                    <Breadcrumb className="mb-3">
                        <Breadcrumb.Item href="/">
                            <FontAwesomeIcon icon={faHouse}/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item href={`/cart/${orderId}`} active>
                            <FormattedMessage id="title.cart"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <PageTitle id="title.cart"/>
                    <ProductInventory order={order} products={reservedProducts} columnCount={1}/>
                </Container>
            );
        }
    }
};

export default CartPage;
