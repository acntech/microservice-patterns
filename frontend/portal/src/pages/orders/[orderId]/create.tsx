import {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import {useForm} from "react-hook-form";
import {useRouter} from "next/router";
import {Button, Form, Icon, Menu, Message, Segment, Table} from "semantic-ui-react";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../../../fragments";
import {ClientError, ClientResponse, ErrorCode, ErrorPayload, Order, Product, State, Status} from "../../../types";
import {RestConsumer} from "../../../core/consumer";
import {orderReducer, productListReducer, productReducer} from "../../../state/reducers";

const filterProductList = (order: Order, products: Product[]): Product[] => {
    const productIdList = order.items.map(item => item.productId);
    return products
        .filter(product => !productIdList.includes(product.productId));
};

const CreateOrderItemPage: FC = (): ReactElement => {

    const router = useRouter();
    const {formatMessage: t} = useIntl();
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pageStatus, setPageStatus] = useState<Status>(Status.LOADING);
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: Status.LOADING});
    const [productState, productDispatch] = useReducer(productReducer, {status: Status.LOADING});
    const [productListState, productListDispatch] = useReducer(productListReducer, {status: Status.LOADING});
    const [createOrderItemState, setCreateOrderItemState] = useState<State<Order>>({status: Status.PENDING});
    const {orderId: orderIdParam} = router.query;
    const orderId = Array.isArray(orderIdParam) ? orderIdParam[0] : orderIdParam;

    useEffect(() => {
        if (orderId) {
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
    }, []);

    useEffect(() => {
        if (orderState.status === Status.LOADING && productListState.status === Status.LOADING && pageStatus !== Status.LOADING) {
            setPageStatus(Status.LOADING);
        } else if (orderState.status === Status.SUCCESS && productListState.status === Status.SUCCESS && pageStatus === Status.LOADING) {
            setPageStatus(Status.SUCCESS);
        } else if ((orderState.status === Status.FAILED || productListState.status === Status.FAILED) && pageStatus !== Status.FAILED) {
            setPageStatus(Status.FAILED);
        }
    }, [orderState, productListState]);

    useEffect(() => {
        if (createOrderItemState.status === Status.SUCCESS) {
            router.push(`/orders/${orderId}`);
        }
    }, [createOrderItemState]);

    const onFormSubmit = (formData: any) => {
        if (!Object.keys(errors).length && !!orderId) {
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

    const validateOrderItemQuantityField = (value: string): boolean => {
        return value !== '' && !isNaN(Number(value));
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
                <>
                    <Segment basic>
                        <Table celled>
                            <Table.Body>
                                <Table.Row>
                                    <Table.Cell width={2} className="table-header">
                                        <FormattedMessage id="label.product.product-id"/>
                                    </Table.Cell>
                                    <Table.Cell width={10}>{productId}</Table.Cell>
                                </Table.Row>
                                <Table.Row>
                                    <Table.Cell width={2} className="table-header">
                                        <FormattedMessage id="label.product.name"/>
                                    </Table.Cell>
                                    <Table.Cell width={10}>{name}</Table.Cell>
                                </Table.Row>
                                <Table.Row>
                                    <Table.Cell width={2} className="table-header">
                                        <FormattedMessage id="label.product.description"/>
                                    </Table.Cell>
                                    <Table.Cell width={10}>{description}</Table.Cell>
                                </Table.Row>
                                <Table.Row>
                                    <Table.Cell width={2} className="table-header">
                                        <FormattedMessage id="label.product.stock"/>
                                    </Table.Cell>
                                    <Table.Cell width={10}>{stock}</Table.Cell>
                                </Table.Row>
                                <Table.Row>
                                    <Table.Cell width={2} className="table-header">
                                        <FormattedMessage id="label.product.unit-price"/>
                                    </Table.Cell>
                                    <Table.Cell width={10}>{currency} {price.toFixed(2)}</Table.Cell>
                                </Table.Row>
                            </Table.Body>
                        </Table>
                    </Segment>
                    <Segment basic>
                        <Form onSubmit={handleSubmit(onFormSubmit)} error={!!Object.keys(errors).length}>
                            <Form.Group>
                                <Form.Field error={!!errors.orderItemQuantity}>
                                    <label>{t({id: 'form.create-order-item.field.quantity.label'})}</label>
                                    <input type="text" size={20}
                                           {...register("orderItemQuantity", {
                                               required: true,
                                               min: 1,
                                               max: product.stock,
                                               validate: validateOrderItemQuantityField
                                           })} />
                                </Form.Field>
                                <Form.Field>
                                    <input type="hidden" value={productId} {...register("productId")} />
                                </Form.Field>
                            </Form.Group>
                            <Form.Group>
                                <Form.Button primary size="tiny">
                                    <Icon name="dolly"/><FormattedMessage id="button.submit"/>
                                </Form.Button>
                                <Button secondary size="tiny" onClick={onCancelButtonClick}>
                                    <Icon name="cancel"/><FormattedMessage id="button.cancel"/>
                                </Button>
                            </Form.Group>
                            <Message error><Icon name="ban"/> {t({id: 'form.create-order-item.error'})}
                            </Message>
                        </Form>
                    </Segment>
                </>
            )
                ;
        } else {
            const filteredProducts = filterProductList(order, productList);

            return (
                <Segment basic>
                    <Menu>
                        <Menu.Item>
                            <Button secondary size="tiny" onClick={onBackButtonClick}>
                                <Icon name="arrow left"/><FormattedMessage id="button.back"/>
                            </Button>
                        </Menu.Item>
                    </Menu>
                    <Table celled selectable>
                        <Table.Header>
                            <Table.Row>
                                <Table.HeaderCell><FormattedMessage id="label.product.product-id"/></Table.HeaderCell>
                                <Table.HeaderCell><FormattedMessage id="label.product.name"/></Table.HeaderCell>
                                <Table.HeaderCell><FormattedMessage id="label.product.stock"/></Table.HeaderCell>
                                <Table.HeaderCell><FormattedMessage id="label.product.unit-price"/></Table.HeaderCell>
                            </Table.Row>
                        </Table.Header>
                        <Table.Body>
                            {filteredProducts.map((product, index) => {
                                const {productId, name, stock, price, currency} = product;

                                return (
                                    <Table.Row key={index} className="clickable-table-row"
                                               onClick={() => onProductTableRowClick(product)}>
                                        <Table.Cell singleLine>{productId}</Table.Cell>
                                        <Table.Cell singleLine>{name}</Table.Cell>
                                        <Table.Cell>{stock}</Table.Cell>
                                        <Table.Cell singleLine>{currency} {price.toFixed(2)}</Table.Cell>
                                    </Table.Row>
                                );
                            })}
                        </Table.Body>
                    </Table>
                </Segment>
            );
        }
    } else {
        return <></>;
    }
}

export default CreateOrderItemPage;
