import React, {FC, ReactElement, useEffect, useReducer, useState} from "react";
import {useRouter} from "next/router";
import {FieldValues, useForm} from "react-hook-form";
import {FormattedMessage, useIntl} from "react-intl";
import {Alert, Button, Container, Form} from "react-bootstrap";
import {FieldErrors} from "react-hook-form/dist/types/errors";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBan, faDolly, faXmark} from "@fortawesome/free-solid-svg-icons";
import {v4 as uuid} from "uuid";
import {ErrorPanelFragment, LoadingIndicatorFragment} from "../fragments";
import {ClientError, ClientResponse, ErrorPayload, Order, Status} from "../types";
import {RestConsumer} from "../core/consumer";
import {orderReducer} from "../state/reducers";

const CreateOrderPage: FC = (): ReactElement => {

    const router = useRouter();
    const {formatMessage: t} = useIntl();
    const [formSubmitted, setFormSubmitted] = useState<boolean>(false);
    const [formValid, setFormValid] = useState<boolean>(true);
    const [orderState, orderDispatch] = useReducer(orderReducer, {status: Status.PENDING});
    const {register, handleSubmit, formState: {errors: formErrors}} = useForm();

    useEffect(() => {
        if (orderState.status === Status.SUCCESS && !!orderState.data) {
            const {orderId} = orderState.data;
            router.push(`/orders/${orderId}`);
        }
    }, [orderState]);

    const onFormSubmit = (formData: any) => {
        setFormSubmitted(true);
        setFormValid(true);
        const body = {
            customerId: uuid(), // TODO: Remove hardcoded UUID
            name: formData.orderName,
            description: formData.orderDescription
        };
        orderDispatch({status: Status.LOADING})
        RestConsumer.createOrder(body,
            (response: ClientResponse<Order>) => orderDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => orderDispatch({status: Status.FAILED, error: error.response}));
    };
    const onFormError = (formErrors: FieldErrors<FieldValues>) => {
        console.log("FORM ERROR", formErrors);
        setFormSubmitted(true);
        setFormValid(Object.keys(formErrors).length == 0);
    };

    const onCancelButtonClick = () => {
        router.push('/');
    };

    if (orderState.status === Status.LOADING) {
        return <LoadingIndicatorFragment/>;
    } else if (orderState.status === Status.FAILED) {
        return <ErrorPanelFragment error={orderState.error}/>;
    } else if (orderState.status === Status.SUCCESS) {
        if (!!orderState.data) {
            return <></>; // Will be redirected
        } else {
            return <ErrorPanelFragment/>;
        }
    } else {
        return (
            <Container as="main">
                <h2 className="mb-3"><FormattedMessage id="title.create-order"/></h2>

                <Form noValidate validated={formSubmitted && formValid}
                      onSubmit={handleSubmit(onFormSubmit, onFormError)}>
                    <Form.Group className="mb-3">
                        <Form.Label>
                            <FormattedMessage id="form.create-order.field.order-name.label"/>
                        </Form.Label>
                        <Form.Control type="text" isInvalid={formSubmitted && !!formErrors.orderName}
                                      placeholder={t({id: 'form.create-order.field.order-name.placeholder'})}
                                      {...register("orderName", {required: true, minLength: 3, maxLength: 20})}/>
                    </Form.Group>
                    <Form.Group className="mb-4">
                        <Form.Label>
                            <FormattedMessage id="form.create-order.field.order-description.label"/>
                        </Form.Label>
                        <Form.Control type="text" isInvalid={formSubmitted && !!formErrors.orderDescription}
                                      placeholder={t({id: 'form.create-order.field.order-description.placeholder'})}
                                      {...register("orderDescription", {maxLength: 200})}/>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Button variant="primary" type="submit" className="me-2">
                            <FontAwesomeIcon icon={faDolly}/> <FormattedMessage id="button.submit"/>
                        </Button>
                        <Button variant="secondary" onClick={onCancelButtonClick}>
                            <FontAwesomeIcon icon={faXmark}/> <FormattedMessage id="button.cancel"/>
                        </Button>
                    </Form.Group>
                    <Alert variant="danger" hidden={!formSubmitted && formValid}>
                        <FontAwesomeIcon icon={faBan}/> <FormattedMessage id="form.create-order.error"/>
                    </Alert>
                </Form>
            </Container>
        );
    }
};

export default CreateOrderPage;
