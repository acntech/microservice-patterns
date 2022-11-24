import * as React from 'react';
import {FC, ReactElement, useCallback, useEffect, useState} from 'react';
import {useHistory, useParams} from 'react-router';
import {Container} from 'semantic-ui-react';

import {LoadingIndicatorFragment, mapErrorPayload} from "../fragments";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../state/reducer";
import {ShowProductListFragment} from "../fragments/show-product-list";
import {CreateOrderItem, Order, Product, SliceStatus} from "../types";
import {createOrderItem, findProductList, getOrder} from "../state/slices";
import {CreateOrderItemFormFragment} from "../fragments/create-order-item-form";
import {GenericErrorPage} from "./generic-error-page";

const filterProductList = (order: Order, products: Product[]): Product[] => {
    const productIdList = order.items.map(item => item.productId);
    return products
        .filter(product => !productIdList.includes(product.productId));
}

interface PageParams {
    orderId: string;
}

interface PageState {
    status: SliceStatus;
}

export const CreateOrderItemPage: FC = (): ReactElement => {

    const {orderId} = useParams<PageParams>();
    const dispatch = useDispatch();
    const history = useHistory();

    const [pageState, setPageState] = useState<PageState>({status: 'IDLE'});
    const [product, setProduct] = useState<Product>();
    const {order: orderState} = useSelector((state: RootState) => state);
    const {productList: productListState} = useSelector((state: RootState) => state);

    useEffect(() => {
        if ((orderState.status === 'LOADING' || productListState.status === 'LOADING') && pageState.status !== 'LOADING') {
            setPageState({status: 'LOADING'});
        } else if ((orderState.status === 'FAILED' || productListState.status === 'FAILED') && pageState.status !== 'FAILED') {
            setPageState({status: 'FAILED'});
        } else if (orderState.status === 'SUCCESS' && productListState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [orderState, productListState]);

    useEffect(() => {
        dispatch(getOrder(orderId));
    }, [dispatch]);

    useEffect(() => {
        dispatch(findProductList());
    }, [dispatch]);

    const onFormSubmit = useCallback((body: CreateOrderItem) => {
        dispatch(createOrderItem(orderId, body));
        history.push(`/orders/${orderId}`);
    }, []);

    const onProductTableRowClick = (selectedProduct: Product) => {
        setProduct(selectedProduct);
    };

    const onCancelButtonClick = () => {
        history.push('/');
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
    } else if (productListState.status === 'FAILED') {
        if (productListState.error) {
            const sliceError = mapErrorPayload(productListState.error);
            const {errorId, errorCode} = sliceError;
            return <GenericErrorPage errorId={errorId} errorCode={errorCode}/>
        } else {
            return <GenericErrorPage errorCode={'ACNTECH.TECHNICAL.COMMON.ERROR_ENTITY_MISSING'}/>
        }
    } else if (pageState.status === 'SUCCESS') {
        const {data: order} = orderState;
        const {data: productList} = productListState;

        if (product) {
            return (
                <Container>
                    <CreateOrderItemFormFragment product={product}
                                                 onFormSubmit={onFormSubmit}
                                                 onCancelButtonClick={onCancelButtonClick}/>
                </Container>
            );
        } else if (!order) {
            return <GenericErrorPage errorCode={'ACNTECH.FUNCTIONAL.ORDERS.ORDER_NOT_FOUND'}/>;
        } else if (!productList) {
            return <GenericErrorPage errorCode={'ACNTECH.FUNCTIONAL.PRODUCTS.PRODUCTS_NOT_FOUND'}/>;
        } else {
            const filteredProducts = filterProductList(order, productList);

            return (
                <Container>
                    <ShowProductListFragment products={filteredProducts}
                                             onTableRowClick={onProductTableRowClick}
                                             onBackButtonClick={onBackButtonClick}/>
                </Container>
            );
        }
    } else {
        return <></>;
    }
};
