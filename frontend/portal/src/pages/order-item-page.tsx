import * as React from 'react';
import {FC, ReactElement, useEffect, useState} from 'react';
import {useHistory, useParams} from 'react-router';
import {Container} from 'semantic-ui-react';
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../state/reducer";
import {LoadingIndicatorFragment, mapErrorPayload, ShowOrderItemFragment} from "../fragments";
import {GenericErrorPage} from "./generic-error-page";
import {OrderItem, SliceStatus} from "../types";
import {deleteOrderItem, getOrder, getProduct} from "../state/slices";

interface PageParams {
    orderId: string;
    itemId: string;
}

interface PageState {
    status: SliceStatus;
}

export const OrderItemPage: FC = (): ReactElement => {

    const {orderId, itemId} = useParams<PageParams>();
    const dispatch = useDispatch();
    const history = useHistory();

    const [pageState, setPageState] = useState<PageState>({status: 'LOADING'});
    const [orderItem, setOrderItem] = useState<OrderItem>();
    const {order: orderState} = useSelector((state: RootState) => state);
    const {product: productState} = useSelector((state: RootState) => state);

    useEffect(() => {
        dispatch(getOrder(orderId));
    }, [dispatch]);

    useEffect(() => {
        if (orderState.status === 'SUCCESS' && !!orderState.data) {
            const selectedOrderItem = orderState.data.items.find(item => item.itemId === itemId);
            if (!!selectedOrderItem) {
                dispatch(getProduct(selectedOrderItem.productId));
                setOrderItem(selectedOrderItem);
            }
        }
    }, [orderState]);

    useEffect(() => {
        if ((orderState.status === 'LOADING' || productState.status === 'LOADING') && pageState.status !== 'LOADING') {
            setPageState({status: 'LOADING'});
        } else if ((orderState.status === 'FAILED' || productState.status === 'FAILED') && pageState.status !== 'FAILED') {
            setPageState({status: 'FAILED'});
        } else if (orderState.status === 'SUCCESS' && productState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [orderState, productState]);

    const onDeleteOrderItemButtonClick = () => {
        dispatch(deleteOrderItem(itemId));
        history.push(`/orders/${orderId}`);
    };

    const onRefreshButtonClick = () => {
        dispatch(getOrder(orderId));
    };

    const onBackButtonClick = () => {
        history.push(`/orders/${orderId}`);
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
    } else if (productState.status === 'FAILED') {
        if (productState.error) {
            const sliceError = mapErrorPayload(productState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.COMMON.ERROR_ENTITY_MISSING'}/>
        }
    } else if (pageState.status === 'SUCCESS') {
        if (!orderState.data) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.ORDERS.BUSINESS_ENTITY_MISSING'}/>
        } else if (!productState.data) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.PRODUCTS.BUSINESS_ENTITY_MISSING'}/>
        } else if (!orderItem) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.ORDERS.ORDER_ITEM_NOT_FOUND'}/>
        } else {
            return (
                <Container>
                    <ShowOrderItemFragment orderItem={orderItem}
                                           product={productState.data}
                                           onBackButtonClick={onBackButtonClick}
                                           onDeleteButtonClick={onDeleteOrderItemButtonClick}
                                           onRefreshButtonClick={onRefreshButtonClick}/>
                </Container>
            );
        }
    } else {
        return <></>;
    }
};