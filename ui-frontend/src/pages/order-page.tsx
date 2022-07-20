import * as React from 'react';
import {FC, ReactElement, useEffect, useState} from 'react';
import {useHistory, useParams} from 'react-router';
import {Container} from 'semantic-ui-react';
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../state/reducer";
import {LoadingIndicatorFragment, mapErrorPayload, ShowOrderFragment} from "../fragments";
import {GenericErrorPage} from "./";
import {deleteOrder, findProductList, getOrder} from "../state/slices";
import {OrderItem, SliceStatus} from "../types";

interface PageParams {
    orderId: string;
}

interface PageState {
    status: SliceStatus;
}

export const OrderPage: FC = (): ReactElement => {

    const {orderId} = useParams<PageParams>();
    const dispatch = useDispatch();
    const history = useHistory();

    const [pageState, setPageState] = useState<PageState>({status: 'LOADING'});
    const {order: orderState} = useSelector((state: RootState) => state);
    const {productList: productListState} = useSelector((state: RootState) => state);

    useEffect(() => {
        dispatch(getOrder(orderId));
    }, [dispatch]);

    useEffect(() => {
        dispatch(findProductList());
    }, [dispatch]);

    useEffect(() => {
        if ((orderState.status === 'LOADING' || productListState.status === 'LOADING') && pageState.status !== 'LOADING') {
            setPageState({status: 'LOADING'});
        } else if ((orderState.status === 'FAILED' || productListState.status === 'FAILED') && pageState.status !== 'FAILED') {
            setPageState({status: 'FAILED'});
        } else if (orderState.status === 'SUCCESS' && productListState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [orderState, productListState]);

    const onRefreshOrderButtonClick = () => {
        dispatch(getOrder(orderId));
        dispatch(findProductList());
    };

    const onConfirmOrderButtonClick = () => {
        history.push(`/orders/${orderId}/items`);
    };

    const onCancelOrderButtonClick = () => {
        dispatch(deleteOrder(orderId));
        history.push(`/orders/${orderId}`);
    };

    const onCreateOrderItemButtonClick = () => {
        history.push(`/orders/${orderId}/create`);
    };

    const onOrderItemTableRowClick = (orderItem: OrderItem) => {
        const {itemId} = orderItem;
        history.push(`/orders/${orderId}/items/${itemId}`);
    };

    const onBackButtonClick = () => {
        history.push('/');
    };

    if (pageState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (orderState.status === 'FAILED') {
        if (orderState.error) {
            const sliceError = mapErrorPayload(orderState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.COMMON.ERROR_ENTITY_MISSING'}/>
        }
    } else if (productListState.status === 'FAILED') {
        if (productListState.error) {
            const sliceError = mapErrorPayload(productListState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.COMMON.ERROR_ENTITY_MISSING'}/>
        }
    } else if (orderState.status === 'SUCCESS') {
        if (!orderState.data) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.ORDERS.BUSINESS_ENTITY_MISSING'}/>
        } else if (!productListState.data) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.PRODUCTS.BUSINESS_ENTITY_MISSING'}/>
        } else {
            return (
                <Container>
                    <ShowOrderFragment order={orderState.data}
                                       products={productListState.data}
                                       onBackButtonClick={onBackButtonClick}
                                       onCreateOrderItemButtonClick={onCreateOrderItemButtonClick}
                                       onConfirmOrderButtonClick={onConfirmOrderButtonClick}
                                       onCancelOrderButtonClick={onCancelOrderButtonClick}
                                       onRefreshOrderButtonClick={onRefreshOrderButtonClick}
                                       onOrderItemTableRowClick={onOrderItemTableRowClick}/>
                </Container>
            );
        }
    } else {
        return <></>;
    }
};
