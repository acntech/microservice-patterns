import React, {FC, ReactElement, useEffect} from "react";
import {useRouter} from "next/router";
import {FormattedMessage} from "react-intl";
import {Col, Container, Row} from "react-bootstrap";
import {OrderListDataMissingError, Status} from "../../types";
import {ErrorPage, LoadingPage, PageTitle} from "../../components";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {findOrders, orderListSelector} from "../../state/order-list-slice";

const CartListPage: FC = (): ReactElement => {

    const router = useRouter();
    const dispatch = useAppDispatch();
    const orderListState = useAppSelector(orderListSelector);

    useEffect(() => {
        dispatch(findOrders());
    }, [dispatch]);

    if (orderListState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (orderListState.status === Status.FAILED) {
        return <ErrorPage error={orderListState.error}/>;
    } else {
        const {data: orders} = orderListState;
        if (!orders) {
            return <ErrorPage error={OrderListDataMissingError}/>;
        } else if (orders.length == 0) {
            return (
                <Container as="main">
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
            const {orderId} = orders[0];
            router.push(`/cart/${orderId}`)
            return <></>;
        }
    }
};

export default CartListPage;
