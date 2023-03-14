import {FC, ReactElement, useEffect, useReducer} from "react";
import {useRouter} from "next/router";
import {useForm} from "react-hook-form";
import {FormattedMessage, useIntl} from "react-intl";
import {Button, Form, Icon, Message, Segment} from "semantic-ui-react";
import {v4 as uuid} from "uuid";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../fragments";
import {ClientError, ClientResponse, ErrorPayload, Order} from "../types";
import {RestConsumer} from "../core/consumer";
import {orderReducer} from "../state/reducers";

const CreateOrderPage: FC = (): ReactElement => {

    const router = useRouter();
    const {formatMessage: t} = useIntl();
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: 'PENDING'});
    const {register, handleSubmit, formState: {errors: formErrors}} = useForm();

    useEffect(() => {
        if (orderState.status === 'SUCCESS' && !!orderState.data) {
            const {orderId} = orderState.data;
            router.push(`/orders/${orderId}`);
        }
    }, [orderState]);

    const onFormSubmit = (formData: any) => {
        const body = {
            customerId: uuid(), // TODO: Remove hardcoded UUID
            name: formData.orderName,
            description: formData.orderDescription
        };
        orderDispatch({status: 'LOADING'})
        RestConsumer.createOrder(body,
            (response: ClientResponse<Order>) => orderDispatch({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: 'FAILED', error: error.response}));
    };
    const onFormError = (formErrors: any) => {
        console.log("FORM ERROR", formErrors)
    };

    const onCancelButtonClick = () => {
        router.push('/');
    };

    if (orderState.status === 'LOADING') {
        return <LoadingIndicatorFragment/>;
    } else if (orderState.status === 'FAILED') {
        return (
            <ErrorPanelFragment error={orderState.error}/>
        );
    } else if (orderState.status === 'SUCCESS') {
        if (!!orderState.data) {
            return <></>; // Will be redirected
        } else {
            return (
                <ErrorPanelFragment/>
            );
        }
    } else {
        return (
            <Segment basic>
                <Form onSubmit={handleSubmit(onFormSubmit, onFormError)}
                      error={!!Object.keys(formErrors).length}>
                    <Form.Group>
                        <Form.Field error={!!formErrors.orderName}>
                            <label>{t({id: 'form.create-order.field.order-name.label'})}</label>
                            <input type="text" size={20}
                                   placeholder={t({id: 'form.create-order.field.order-name.placeholder'})}
                                   {...register("orderName", {required: true, minLength: 4, maxLength: 20})} />
                        </Form.Field>
                    </Form.Group>
                    <Form.Group>
                        <Form.Field error={!!formErrors.orderDescription}>
                            <label>{t({id: 'form.create-order.field.order-description.label'})}</label>
                            <input type="text" size={60}
                                   placeholder={t({id: 'form.create-order.field.order-description.placeholder'})}
                                   {...register("orderDescription", {maxLength: 200})} />
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
                    <Message error><Icon name="ban"/> {t({id: 'form.create-order.error'})}</Message>
                </Form>
            </Segment>
        );
    }
};

export default CreateOrderPage;
