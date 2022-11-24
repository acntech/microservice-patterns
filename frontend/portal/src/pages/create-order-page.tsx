import * as React from 'react';
import {FC, ReactElement, useCallback, useEffect, useState} from 'react';
import {Redirect, useHistory} from 'react-router';
import {useDispatch, useSelector} from "react-redux";

import {Container} from 'semantic-ui-react';
import {CreateOrderFormFragment, LoadingIndicatorFragment, mapErrorPayload} from "../fragments";
import {createOrder} from "../state/slices";
import {CreateOrder, SliceStatus} from "../types";
import {RootState} from "../state/reducer";
import {GenericErrorPage} from "./";

interface PageState {
    status: SliceStatus;
}

export const CreateOrderPage: FC = (): ReactElement => {

    const dispatch = useDispatch();
    const history = useHistory();

    const [pageState, setPageState] = useState<PageState>({status: 'IDLE'});
    const {order: orderState} = useSelector((state: RootState) => state);

    useEffect(() => {
        if (orderState.status === 'LOADING' && pageState.status !== 'LOADING') {
            setPageState({status: 'LOADING'});
        } else if (orderState.status === 'FAILED' && pageState.status !== 'FAILED') {
            setPageState({status: 'FAILED'});
        } else if (orderState.status === 'SUCCESS' && pageState.status !== 'SUCCESS') {
            setPageState({status: 'SUCCESS'});
        }
    }, [orderState]);

    const onFormSubmit = useCallback((body: CreateOrder) => {
        setPageState({status: 'LOADING'});
        dispatch(createOrder(body));
    }, []);

    const onCancelButtonClick = () => {
        history.push('/');
    };

    if (pageState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (pageState.status === 'FAILED') {
        if (orderState.error) {
            const sliceError = mapErrorPayload(orderState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.FUNCTIONAL.ORDERS.ERROR_ENTITY_MISSING'}/>
        }
    } else if (pageState.status === 'SUCCESS') {
        if (orderState.data) {
            const {orderId} = orderState.data;
            return <Redirect to={`/orders/${orderId}`}/>;
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.FUNCTIONAL.ORDERS.ORDER_NOT_FOUND'}/>
        }
    } else {
        return (
            <Container>
                <CreateOrderFormFragment onFormSubmit={onFormSubmit} onCancelButtonClick={onCancelButtonClick}/>
            </Container>
        );
    }
};
