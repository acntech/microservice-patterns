import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage, useIntl} from 'react-intl';
import {Button, Form, Icon, Message, Segment} from 'semantic-ui-react';
import {CreateOrderItem, Product} from "../types";
import {useForm} from "react-hook-form";
import {ShowProductFragment} from "./show-product";

interface FragmentProps {
    product: Product;
    onFormSubmit: (createOrderItem: CreateOrderItem) => void;
    onCancelButtonClick: () => void;
}

export const CreateOrderItemFormFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {product, onFormSubmit, onCancelButtonClick} = props;

    const {formatMessage: t} = useIntl();
    const {register, handleSubmit, formState: {errors}} = useForm();

    const mapFormValuesAndSubmit = (formData: any) => {
        if (!Object.keys(errors).length) {
            onFormSubmit({
                productId: product.productId,
                quantity: formData.orderItemQuantity
            });
        }
    }

    const validateOrderItemQuantityField = (value: string): boolean => {
        return value !== '' && !isNaN(Number(value));
    }

    const orderItemQuantityField = {
        name: "orderItemQuantity",
        type: "text",
        size: 20,
        label: t({id: 'fragment.create-order-item-form.field.quantity.label'}),
        options: {required: true, min: 1, max: product.stock, validate: validateOrderItemQuantityField}
    }

    return (
        <>
            <ShowProductFragment product={product}/>
            <Segment basic>
                <Form onSubmit={handleSubmit(mapFormValuesAndSubmit)} error={!!Object.keys(errors).length}>
                    <Form.Group>
                        <Form.Field error={!!errors.orderItemQuantity}>
                            <label>{orderItemQuantityField.label}</label>
                            <input
                                type={orderItemQuantityField.type}
                                size={orderItemQuantityField.size}
                                {...register(orderItemQuantityField.name, orderItemQuantityField.options)} />
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
                    <Message error><Icon name="ban"/> {t({id: 'fragment.create-order-item-form.error'})}</Message>
                </Form>
            </Segment>
        </>
    );
};