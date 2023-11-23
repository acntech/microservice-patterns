import React, {FC, ReactElement, useEffect} from "react";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {ErrorPage, LoadingPage, OrderDetails, PageTitle, ProductInventory} from "../components";
import {ProductListDataMissingError, Status} from "../types";
import {orderSelector, setOrder} from "../state/order-slice";
import {findOrders, orderListSelector} from "../state/order-list-slice";
import {useAppDispatch, useAppSelector} from "../state/store";
import {findProducts, productListSelector} from "../state/product-list-slice";

const ProductsPage: FC = (): ReactElement => {

    const dispatch = useAppDispatch();
    const orderState = useAppSelector(orderSelector);
    const orderListState = useAppSelector(orderListSelector);
    const productListState = useAppSelector(productListSelector);

    useEffect(() => {
        dispatch(findOrders());
    }, [dispatch]);

    useEffect(() => {
        dispatch(findProducts());
    }, [dispatch]);

    useEffect(() => {
        if (orderListState.status === Status.SUCCESS && !!orderListState.data && orderListState.data.length > 0) {
            dispatch(setOrder({order: orderListState.data[0]}));
        }
    }, [orderListState]);

    if (orderState.status === Status.LOADING || orderListState.status === Status.LOADING || productListState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (orderState.status === Status.FAILED) {
        return <ErrorPage error={orderState.error}/>
    } else if (orderListState.status === Status.FAILED) {
        return <ErrorPage error={orderListState.error}/>
    } else if (productListState.status === Status.FAILED) {
        return <ErrorPage error={productListState.error}/>
    } else if (orderListState.status === Status.SUCCESS && productListState.status === Status.SUCCESS) {
        const {data: order} = orderState;
        const {data: products} = productListState;
        if (!products) {
            return <ErrorPage error={ProductListDataMissingError}/>
        } else {
            return (
                <Container as="main">
                    <Breadcrumb className="mb-3"></Breadcrumb>
                    <PageTitle id="title.products"/>
                    <Row>
                        <Col sm={8}>
                            <ProductInventory order={order} products={products} columnCount={2}/>
                        </Col>
                        <Col sm={3} className="border-start ps-4">
                            <OrderDetails order={order} products={products}/>
                        </Col>
                    </Row>
                </Container>
            );
        }
    } else {
        return <></>
    }
};

export default ProductsPage;
