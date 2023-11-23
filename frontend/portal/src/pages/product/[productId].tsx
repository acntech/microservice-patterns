import React, {FC, ReactElement, useEffect, useState} from "react";
import {useRouter} from "next/router";
import {FormattedMessage} from "react-intl";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouse} from "@fortawesome/free-solid-svg-icons";
import {ErrorPage, LoadingPage, OrderDetails, PageTitle, ProductDetails} from "../../components";
import {Order, Product, ProductDataMissingError, ProductListDataMissingError, Status} from "../../types";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {orderSelector} from "../../state/order-slice";
import {findOrders, orderListSelector} from "../../state/order-list-slice";
import {findProducts, productListSelector} from "../../state/product-list-slice";

const ProductPage: FC = (): ReactElement => {

    const router = useRouter();
    const dispatch = useAppDispatch();
    const orderState = useAppSelector(orderSelector);
    const orderListState = useAppSelector(orderListSelector);
    const productListState = useAppSelector(productListSelector);
    const [order, setOrder] = useState<Order>();
    const [product, setProduct] = useState<Product>();
    const {productId: productIdParam} = router.query;
    const productId = Array.isArray(productIdParam) ? productIdParam[0] : productIdParam;

    useEffect(() => {
        dispatch(findOrders());
    }, [dispatch]);

    useEffect(() => {
        dispatch(findProducts());
    }, [dispatch]);

    useEffect(() => {
        if (orderState.status === Status.SUCCESS && !!orderState.data) {
            setOrder(orderState.data);
        }
    }, [orderState]);

    useEffect(() => {
        if (orderListState.status === Status.SUCCESS && !!orderListState.data && orderListState.data.length > 0) {
            setOrder(orderListState.data[0]);
        }
    }, [orderListState]);

    useEffect(() => {
        if (!!productId && productListState.status === Status.SUCCESS && !!productListState.data) {
            setProduct(productListState.data.find((p) => p?.productId === productId));
        }
    }, [productId, productListState]);

    if (orderState.status === Status.LOADING || orderListState.status === Status.LOADING || productListState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (orderState.status === Status.FAILED) {
        return <ErrorPage error={orderState.error}/>
    } else if (orderListState.status === Status.FAILED) {
        return <ErrorPage error={orderListState.error}/>
    } else if (productListState.status === Status.FAILED) {
        return <ErrorPage error={productListState.error}/>
    } else {
        const products = productListState.data;

        if (!product) {
            return <ErrorPage error={ProductDataMissingError}/>
        } else if (!products) {
            return <ErrorPage error={ProductListDataMissingError}/>
        } else {
            return (
                <Container as="main">
                    <Breadcrumb className="mb-3">
                        <Breadcrumb.Item href="/">
                            <FontAwesomeIcon icon={faHouse}/>
                        </Breadcrumb.Item>
                        <Breadcrumb.Item href={`/product/${productId}`} active>
                            <FormattedMessage id="title.product"/>
                        </Breadcrumb.Item>
                    </Breadcrumb>
                    <PageTitle id="title.product"/>
                    <Row>
                        <Col sm={8}>
                            <ProductDetails detailed={true} order={order} product={product}/>
                        </Col>
                        <Col sm={3} className="border-start ps-4">
                            <OrderDetails order={order} products={products}/>
                        </Col>
                    </Row>
                </Container>
            );
        }
    }
};

export default ProductPage;
