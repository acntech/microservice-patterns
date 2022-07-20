import * as React from 'react';
import {FC, ReactElement} from 'react';
import {FormattedMessage, useIntl} from 'react-intl';
import {Button, Form, Icon, Message, Segment} from 'semantic-ui-react';
import {useForm} from 'react-hook-form';
import {v4 as uuid} from 'uuid';
import {CreateOrder} from '../types';

interface FragmentProps {
    onFormSubmit: (createOrder: CreateOrder) => void;
    onCancelButtonClick: () => void;
}

export const CreateOrderFormFragment: FC<FragmentProps> = (props: FragmentProps): ReactElement => {
    const {onFormSubmit, onCancelButtonClick} = props;

    const {formatMessage: t} = useIntl();
    const {register, handleSubmit, formState: {errors}} = useForm();

    const orderNameField = {
        name: "orderName",
        type: "text",
        size: 20,
        label: t({id: 'fragment.create-order-form.field.order-name.label'}),
        placeholder: t({id: 'fragment.create-order-form.field.order-name.placeholder'}),
        options: {required: true, minLength: 4, maxLength: 20}
    }
    const orderDescriptionField = {
        name: "orderDescription",
        type: "text",
        size: 60,
        label: t({id: 'fragment.create-order-form.field.order-description.label'}),
        placeholder: t({id: 'fragment.create-order-form.field.order-description.placeholder'}),
        options: {maxLength: 200}
    }

    const mapFormValuesAndSubmit = (formData: any) => {
        if (!Object.keys(errors).length) {
            onFormSubmit({
                customerId: uuid(),
                name: formData.orderName,
                description: formData.orderDescription
            });
        }
    };

    return (
        <Segment basic>
            <Form onSubmit={handleSubmit(mapFormValuesAndSubmit)} error={!!Object.keys(errors).length}>
                <Form.Group>
                    <Form.Field error={!!errors.orderName}>
                        <label>{orderNameField.label}</label>
                        <input
                            type={orderNameField.type}
                            size={orderNameField.size}
                            placeholder={orderNameField.placeholder}
                            {...register(orderNameField.name, orderNameField.options)} />
                    </Form.Field>
                </Form.Group>
                <Form.Group>
                    <Form.Field error={!!errors.orderDescription}>
                        <label>{orderDescriptionField.label}</label>
                        <input
                            type={orderDescriptionField.type}
                            size={orderDescriptionField.size}
                            placeholder={orderDescriptionField.placeholder}
                            {...register(orderDescriptionField.name, orderDescriptionField.options)} />
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
                <Message error><Icon name="ban"/> {t({id: 'fragment.create-order-form.error'})}</Message>
            </Form>
        </Segment>
    );
};
