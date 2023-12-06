import React, {FC, ReactElement, useContext, useEffect, useState} from "react";
import {Breadcrumb, Col, Container, Row} from "react-bootstrap";
import {ErrorPage, LoadingPage, OrderDetails, OrderList, PageTitle, ProductInventory} from "../components";
import {Order, OrderStatus, ProductListDataMissingStateError, Status} from "../types";
import {orderSelector, setOrder} from "../state/order-slice";
import {findOrders, orderListSelector} from "../state/order-list-slice";
import {useAppDispatch, useAppSelector} from "../state/store";
import {findProducts, productListSelector} from "../state/product-list-slice";
import {Session} from "../providers";

const ProductsPage: FC = (): ReactElement => {

    const dispatch = useAppDispatch();
    const {userContext} = useContext(Session);
    const orderState = useAppSelector(orderSelector);
    const orderListState = useAppSelector(orderListSelector);
    const productListState = useAppSelector(productListSelector);
    const [openOrders, setOpenOrders] = useState<Order[]>([])
    const [closesOrders, setClosedOrders] = useState<Order[]>([])
    const {uid: customerId} = userContext;


    useEffect(() => {
        dispatch(findOrders({customerId}));
    }, [dispatch, customerId]);

    useEffect(() => {
        dispatch(findProducts());
    }, [dispatch]);

    useEffect(() => {
        if (orderListState.status === Status.SUCCESS) {
            const {data: orders} = orderListState;
            const filteredOpenOrders = orders?.filter(order => order.status === OrderStatus.PENDING) || [];
            const filteredClosesOrders = orderListState.data?.filter(order => order.status === OrderStatus.CONFIRMED) || [];
            setOpenOrders(filteredOpenOrders);
            setClosedOrders(filteredClosesOrders);
        }
    }, [dispatch, orderListState]);

    useEffect(() => {
        const order = openOrders.length > 0 ? openOrders[0] : undefined;
        dispatch(setOrder({order}));
    }, [dispatch, openOrders]);

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
            return <ErrorPage error={ProductListDataMissingStateError}/>
        } else if (openOrders.length > 1) {
            return (
                <Container as="main">
                    <Breadcrumb className="mb-3"></Breadcrumb>
                    <PageTitle id="title.orders"/>
                    <OrderList orders={openOrders} products={products}/>
                </Container>
            );
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
