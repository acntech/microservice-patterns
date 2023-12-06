import React, {FC, ReactElement, useContext, useEffect} from "react";
import {useRouter} from "next/router";
import {FormattedMessage} from "react-intl";
import {Col, Container, Row} from "react-bootstrap";
import {OrderListDataMissingStateError, OrderStatus, Status} from "../../types";
import {ErrorPage, LoadingPage, PageTitle} from "../../components";
import {useAppDispatch, useAppSelector} from "../../state/store";
import {findOrders, orderListSelector} from "../../state/order-list-slice";
import {Session} from "../../providers";

const CartListPage: FC = (): ReactElement => {

    const router = useRouter();
    const dispatch = useAppDispatch();
    const {userContext} = useContext(Session);
    const orderListState = useAppSelector(orderListSelector);
    const {uid: customerId} = userContext;

    useEffect(() => {
        dispatch(findOrders({customerId}));
    }, [dispatch, customerId]);

    if (orderListState.status === Status.LOADING) {
        return <LoadingPage/>;
    } else if (orderListState.status === Status.FAILED) {
        return <ErrorPage error={orderListState.error}/>;
    } else {
        const {data: orders} = orderListState;
        if (!orders) {
            return <ErrorPage error={OrderListDataMissingStateError}/>;
        } else {
            const openOrders = orders.filter(order => order.status === OrderStatus.PENDING);
            if (openOrders.length == 0) {
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
                const {orderId} = openOrders[0];
                router.push(`/cart/${orderId}`)
                return <LoadingPage/>;
            }
        }
    }
};

export default CartListPage;
