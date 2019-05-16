import * as React from 'react';
import { Component, ReactNode } from 'react';
import { FormattedMessage, injectIntl } from 'react-intl';
import { Button, Form, Icon, InputOnChangeData, Message, Segment, Table } from 'semantic-ui-react';
import { Currency, FormData, FormElementData, Product } from '../../models';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentParamProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputQuantityChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    product: Product;
    formData: CreateItemFormData;
}

type ComponentProps = ComponentParamProps & InjectedIntlProps;

export interface CreateItemFormData extends FormData {
    formInputQuantity: FormElementData;
}

const initialCreateItemFormElementData: FormElementData = {
    formElementError: false,
    formElementValue: ''
};

export const initialCreateItemFormData: CreateItemFormData = {
    formSubmitted: false,
    formError: false,
    formInputQuantity: initialCreateItemFormElementData
};

class CreateItemFormComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            onCancelButtonClick,
            onFormSubmit,
            onFormInputQuantityChange,
            product,
            formData,
            intl
        } = this.props;
        const {productId, name, description, stock, price, currency} = product;
        const {
            formError,
            formErrorMessage,
            formInputQuantity
        } = formData;
        const {
            formElementError: formQuantityError,
            formElementValue: formQuantityValue
        } = formInputQuantity;
        const itemQuantityText = intl.formatMessage({id: 'label.item-quantity.text'});
        const itemQuantityPlaceholderText = intl.formatMessage({id: 'form.placeholder.item-quantity.text'});

        return (
            <Segment basic>
                <Table celled>
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.product-id.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{productId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.product-name.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{name}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.product-description.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{description}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.product-stock.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{stock}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2} className="table-header">
                                <FormattedMessage id="label.product-price.text" />
                            </Table.Cell>
                            <Table.Cell width={10}>{Currency[currency]} {price.toFixed(2)}</Table.Cell>
                        </Table.Row>
                    </Table.Body>
                </Table>
                <Form onSubmit={onFormSubmit} error={formError}>
                    <Form.Group>
                        <Form.Input
                            error={formQuantityError}
                            width={10}
                            label={itemQuantityText}
                            placeholder={itemQuantityPlaceholderText}
                            value={formQuantityValue}
                            onChange={onFormInputQuantityChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Button primary size="tiny">
                            <Icon name="dolly" /><FormattedMessage id="button.add-item.text" />
                        </Form.Button>
                        <Button secondary size="tiny" onClick={onCancelButtonClick}>
                            <Icon name="cancel" /><FormattedMessage id="button.cancel.text" />
                        </Button>
                    </Form.Group>
                    <Message error><Icon name="ban" /> {formErrorMessage}</Message>
                </Form>
            </Segment>
        );
    }
}

const IntlCreateItemFormComponent = injectIntl(CreateItemFormComponent);

export { IntlCreateItemFormComponent as CreateItemForm };