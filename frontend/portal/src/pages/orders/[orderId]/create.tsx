import {FC, ReactElement, useEffect, useState} from "react";
import {FormattedMessage, useIntl} from "react-intl";
import {useForm} from "react-hook-form";
import {useRouter} from "next/router";
import {Button, Form, Icon, Menu, Message, Segment, Table} from "semantic-ui-react";
import {ErrorPanelFragment, LoadingIndicatorFragment, mapErrorPayload} from "../../../fragments";
import {ClientError, CreateOrderItem, ErrorPayload, Order, PageState, Product} from "../../../types";
import {RestClient} from "../../../core/client";

const filterProductList = (order: Order, products: Product[]): Product[] => {
    const productIdList = order.items.map(item => item.productId);
    return products
        .filter(product => !productIdList.includes(product.productId));
};

const CreateOrderItemPage: FC = (): ReactElement => {

    const router = useRouter();
    const {formatMessage: t} = useIntl();
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pageState, setPageState] = useState<PageState<Product>>({status: 'LOADING'});
    const [getOrderState, setGetOrderState] = useState<PageState<Order>>({status: 'LOADING'});
    const [getProductListState, setGetProductListState] = useState<PageState<Product[]>>({status: 'LOADING'});
    const [createOrderItemState, setCreateOrderItemState] = useState<PageState<Order>>({status: 'PENDING'});
    const {orderId} = router.query

    const getOrder = () => {
        setGetOrderState({status: 'LOADING', data: undefined});
        RestClient.GET<Order>(`/api/orders/${orderId}`)
            .then(response => {
                setGetOrderState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetOrderState({status: 'FAILED', error: error.response?.body});
            });
    };

    const getProducts = () => {
        setGetProductListState({status: 'LOADING', data: undefined});
        RestClient.GET<Product[]>('/api/products')
            .then(response => {
                setGetProductListState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setGetProductListState({status: 'FAILED', error: error.response?.body});
            });
    };

    const createOrderItem = (body: CreateOrderItem) => {
        setGetOrderState({status: 'LOADING', data: undefined});
        RestClient.POST<Order>(`/api/orders/${orderId}/items`, body)
            .then(response => {
                setCreateOrderItemState({status: 'SUCCESS', data: response.body});
            })
            .catch(e => {
                const error = e as ClientError<ErrorPayload>;
                setCreateOrderItemState({status: 'FAILED', error: error.response?.body});
            });
    };

    useEffect(() => {
        getOrder();
        getProducts();
    }, []);

    useEffect(() => {
        if ((getOrderState.status === 'FAILED' || getProductListState.status === 'FAILED') && pageState.status === 'LOADING') {
            setPageState({status: 'FAILED'});
        } else if (getOrderState.status === 'SUCCESS' && getProductListState.status === 'SUCCESS' && pageState.status === 'LOADING') {
            setPageState({status: 'SUCCESS'});
        }
    }, [getOrderState, getProductListState]);

    useEffect(() => {
        if (createOrderItemState.status === 'SUCCESS') {
            router.push(`/orders/${orderId}`);
        }
    }, [createOrderItemState]);

    const onFormSubmit = (formData: any) => {
        if (!Object.keys(errors).length) {
            createOrderItem({
                productId: formData.productId,
                quantity: formData.orderItemQuantity
            });
        }
    };

    const onProductTableRowClick = (selectedProduct: Product) => {
        setPageState({...pageState, data: selectedProduct});
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

    if (pageState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (pageState.status === 'FAILED') {
        if (!!getOrderState.error) {
            const {errorId, errorCode} = mapErrorPayload(getOrderState.error);
            return <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
        } else if (!!getProductListState.error) {
            const {errorId, errorCode} = mapErrorPayload(getProductListState.error);
            return <ErrorPanelFragment errorId={errorId} errorCode={errorCode}/>
        } else {
            return <ErrorPanelFragment errorCode={'ACNTECH.TECHNICAL.COMMON.MISSING_ERROR_RESPONSE'}/>
        }
    } else if (pageState.status === 'SUCCESS') {
        const {data: product} = pageState;
        const {data: order} = getOrderState;
        const {data: productList} = getProductListState;

        if (!order) {
            return <ErrorPanelFragment errorCode={'ACNTECH.FUNCTIONAL.ORDERS.ORDER_NOT_FOUND'}/>;
        } else if (!productList) {
            return <ErrorPanelFragment errorCode={'ACNTECH.FUNCTIONAL.PRODUCTS.PRODUCTS_NOT_FOUND'}/>
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
