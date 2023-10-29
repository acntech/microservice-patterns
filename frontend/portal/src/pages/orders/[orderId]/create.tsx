import React, {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {FormattedMessage} from "react-intl";
import {FieldValues, useForm} from "react-hook-form";
import {useRouter} from "next/router";
import {Alert, Button, Container, Form, Nav, Table} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faBan, faDolly, faXmark} from "@fortawesome/free-solid-svg-icons";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../../../fragments";
import {ClientError, ClientResponse, ErrorCode, ErrorPayload, Order, Product, State, Status} from "../../../types";
import {RestConsumer} from "../../../core/consumer";
import {orderReducer, productListReducer, productReducer} from "../../../state/reducers";
import {FieldErrors} from "react-hook-form/dist/types/errors";

const filterProductList = (order: Order, products: Product[]): Product[] => {
    const productIdList = order.items.map(item => item.productId);
    return products
        .filter(product => !productIdList.includes(product.productId));
};

const CreateOrderItemPage: FC = (): ReactElement => {

    const router = useRouter();
    const {register, handleSubmit, formState: {errors: formErrors}} = useForm();
    const [formSubmitted, setFormSubmitted] = useState<boolean>(false);
    const [formValid, setFormValid] = useState<boolean>(true);
    const [pageStatus, setPageStatus] = useState<Status>(Status.LOADING);
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: Status.LOADING});
    const [productState, productDispatch] = useReducer(productReducer, {status: Status.LOADING});
    const [productListState, productListDispatch] = useReducer(productListReducer, {status: Status.LOADING});
    const [createOrderItemState, setCreateOrderItemState] = useState<State<Order>>({status: Status.PENDING});
    const {orderId: orderIdParam} = router.query;
    const orderId = Array.isArray(orderIdParam) ? orderIdParam[0] : orderIdParam;

    useEffect(() => {
        if (!!orderId) {
            RestConsumer.getOrder(orderId,
                (response: ClientResponse<Order>) =>
                    orderDispatch({status: Status.SUCCESS, data: response}),
                (error: ClientError<ErrorPayload>) =>
                    orderDispatch({status: Status.FAILED, error: error.response}));
            RestConsumer.getProducts(
                (response: ClientResponse<Product[]>) =>
                    productListDispatch({status: Status.SUCCESS, data: response}),
                (error: ClientError<ErrorPayload>) =>
                    productListDispatch({status: Status.FAILED, error: error.response}));
        }
    }, [orderId]);

    useEffect(() => {
        if (orderState.status === Status.LOADING && productListState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderState.status === Status.SUCCESS && productListState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if ((orderState.status === Status.FAILED || productListState.status === Status.FAILED) && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [pageStatus, orderState, productListState]);

    useEffect(() => {
        if (createOrderItemState.status === Status.SUCCESS) {
            router.push(`/orders/${orderId}`);
        }
    }, [createOrderItemState]);

    const onFormSubmit = (formData: any) => {
        setFormSubmitted(true);
        setFormValid(true);
        if (!Object.keys(formErrors).length && !!orderId) {
            setPageStatus(Status.LOADING);
            orderDispatch({status: Status.LOADING, data: undefined});
            const {productId, orderItemQuantity: quantity} = formData;
            RestConsumer.createOrderItem(orderId, {productId, quantity},
                (response: ClientResponse<Order>) =>
                    setCreateOrderItemState({status: Status.SUCCESS, data: response.body}),
                (error: ClientError<ErrorPayload>) =>
                    setCreateOrderItemState({status: Status.FAILED, error: error.response?.body}));
        }
    };

    const onFormError = (formErrors: FieldErrors<FieldValues>) => {
        console.log("FORM ERROR", formErrors);
        setFormSubmitted(true);
        setFormValid(Object.keys(formErrors).length == 0);
    };

    const onProductTableRowClick = (selectedProduct: Product) => {
        const data = {status: 200, body: selectedProduct, headers: new Headers}
        productDispatch({status: Status.SUCCESS, data});
    };

    const onCancelButtonClick = () => {
        router.push('/');
    };

    const onBackButtonClick = () => {
        router.push(`/orders/${orderId}`);
    };

    if (pageStatus === Status.LOADING) {
        return <LoadingIndicatorFragment/>;
    } else if (pageStatus === Status.FAILED) {
        return <ErrorPanelFragment error={orderState.error || productListState.error}/>
    } else if (pageStatus === Status.SUCCESS) {
        const product = productState.data;
        const order = orderState.data;
        const productList = productListState.data;

        if (!order) {
            return <ErrorPanelFragment error={{status: ErrorCode.ORDER_DATA_MISSING}}/>;
        } else if (!productList) {
            return <ErrorPanelFragment error={{status: ErrorCode.PRODUCT_LIST_DATA_MISSING}}/>
        } else if (product) {
            const {productId, name, description, stock, currency, price} = product;
            return (
                <Container as="main">
                    <Table bordered>
                        <tbody>
                        <tr>
                            <td className="table-header">
                                <FormattedMessage id="label.product.product-id"/>
                            </td>
                            <td>{productId}</td>
                        </tr>
                        <tr>
                            <td className="table-header">
                                <FormattedMessage id="label.product.name"/>
                            </td>
                            <td>{name}</td>
                        </tr>
                        <tr>
                            <td className="table-header">
                                <FormattedMessage id="label.product.description"/>
                            </td>
                            <td>{description}</td>
                        </tr>
                        <tr>
                            <td className="table-header">
                                <FormattedMessage id="label.product.stock"/>
                            </td>
                            <td width={10}>{stock}</td>
                        </tr>
                        <tr>
                            <td width={2} className="table-header">
                                <FormattedMessage id="label.product.unit-price"/>
                            </td>
                            <td width={10}>{currency} {price.toFixed(2)}</td>
                        </tr>
                        </tbody>
                    </Table>
                    <Form noValidate validated={formSubmitted && formValid}
                          onSubmit={handleSubmit(onFormSubmit, onFormError)}>
                        <Form.Group className="mb-4">
                            <Form.Label>
                                <FormattedMessage id="form.create-order-item.field.quantity.label"/>
                            </Form.Label>
                            <Form.Control type="text" isInvalid={formSubmitted && !!formErrors.orderItemQuantity}
                                          {...register("orderItemQuantity", {
                                              required: true,
                                              min: 1,
                                              max: product.stock
                                          })}/>
                            <Form.Control type="hidden" value={productId} {...register("productId")}/>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Button variant="primary" type="submit" className="me-2">
                                <FontAwesomeIcon icon={faDolly}/><FormattedMessage id="button.submit"/>
                            </Button>
                            <Button variant="secondary" onClick={onCancelButtonClick}>
                                <FontAwesomeIcon icon={faXmark}/><FormattedMessage id="button.cancel"/>
                            </Button>
                        </Form.Group>
                        <Alert variant="danger" hidden={!formSubmitted && formValid}>
                            <FontAwesomeIcon icon={faBan}/> <FormattedMessage id="form.create-order-item.error"/>
                        </Alert>
                    </Form>
                </Container>
            )
                ;
        } else {
            const filteredProducts = filterProductList(order, productList);

            return (
                <Container as="main">
                    <h2 className="mb-3"><FormattedMessage id="title.create-order-item"/></h2>

                    <Nav className="justify-content-end mb-3">
                        <Nav.Item>
                            <Button variant="secondary" onClick={onBackButtonClick}>
                                <FontAwesomeIcon icon={faArrowLeft}/><FormattedMessage id="button.back"/>
                            </Button>
                        </Nav.Item>
                    </Nav>

                    <Table bordered hover>
                        <thead>
                        <tr>
                            <th><FormattedMessage id="label.product.product-id"/></th>
                            <th><FormattedMessage id="label.product.name"/></th>
                            <th><FormattedMessage id="label.product.stock"/></th>
                            <th><FormattedMessage id="label.product.unit-price"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredProducts.map((product, index) => {
                            const {productId, name, stock, price, currency} = product;

                            return (
                                <tr key={index} className="clickable-table-row"
                                    onClick={() => onProductTableRowClick(product)}>
                                    <td>{productId}</td>
                                    <td>{name}</td>
                                    <td>{stock}</td>
                                    <td>{currency} {price.toFixed(2)}</td>
                                </tr>
                            );
                        })}
                        </tbody>
                    </Table>
                </Container>
            );
        }
    } else {
        return <></>;
    }
}

export default CreateOrderItemPage;
