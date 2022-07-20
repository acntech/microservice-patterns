import * as React from 'react';
import {FC, ReactElement, useEffect} from 'react';
import {useHistory} from 'react-router';
import {useDispatch, useSelector} from "react-redux";
import {Container} from 'semantic-ui-react';
import {LoadingIndicatorFragment, mapErrorPayload, ShowOrderListFragment} from "../fragments";
import {findOrderList} from "../state/slices";
import {RootState} from "../state/reducer";
import {GenericErrorPage} from "./generic-error-page";

export const HomePage: FC = (): ReactElement => {

    const dispatch = useDispatch();
    const history = useHistory();

    useEffect(() => {
        dispatch(findOrderList());
    }, [dispatch]);

    const {orderList: orderListState} = useSelector((state: RootState) => state);

    const onTableRowClick = (id: string) => {
        history.push(`/orders/${id}`);
    };

    const onCreateOrderButtonClick = () => {
        history.push(`/create`);
    };

    if (orderListState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (orderListState.status === 'FAILED') {
        if (orderListState.error) {
            const sliceError = mapErrorPayload(orderListState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.FUNCTIONAL.ORDERS.ERROR_ENTITY_MISSING'}/>
        }
    } else if (orderListState.status === 'SUCCESS') {
        if (!orderListState.data) {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.ORDERS.BUSINESS_ENTITY_MISSING'}/>
        } else {
            return (
                <Container>
                    <ShowOrderListFragment
                        orders={orderListState.data || []}
                        onTableRowClick={onTableRowClick}
                        onCreateOrderButtonClick={onCreateOrderButtonClick}/>
                </Container>
            );
        }
    } else {
        return <></>;
    }
};
