import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, Form, Icon, InputOnChangeData, Message, Segment, Table} from 'semantic-ui-react';
import {Currency, FormData, FormElementData, Product} from "../../models";


interface ComponentProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputQuantityChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    product: Product;
    formData: CreateItemFormData;
}

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

class CreateItemFormContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            onCancelButtonClick,
            onFormSubmit,
            onFormInputQuantityChange,
            product,
            formData
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

        return (
            <Segment basic>
                <Table celled>
                    <Table.Body>
                        <Table.Row>
                            <Table.Cell width={2}><b>Product ID</b></Table.Cell>
                            <Table.Cell width={10}>{productId}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Name</b></Table.Cell>
                            <Table.Cell width={10}>{name}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Description</b></Table.Cell>
                            <Table.Cell width={10}>{description}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Stock</b></Table.Cell>
                            <Table.Cell width={10}>{stock}</Table.Cell>
                        </Table.Row>
                        <Table.Row>
                            <Table.Cell width={2}><b>Price</b></Table.Cell>
                            <Table.Cell width={10}>{Currency[currency]} {price.toFixed(2)}</Table.Cell>
                        </Table.Row>
                    </Table.Body>
                </Table>
                <Form onSubmit={onFormSubmit} error={formError}>
                    <Form.Group>
                        <Form.Input
                            error={formQuantityError}
                            width={10}
                            label='Quantity'
                            placeholder='Enter quantity...'
                            value={formQuantityValue}
                            onChange={onFormInputQuantityChange}/>
                    </Form.Group>
                    <Form.Group>
                        <Form.Button
                            primary size='tiny'><Icon name='dolly'/>Add Item</Form.Button>
                        <Button
                            secondary size='tiny'
                            onClick={onCancelButtonClick}><Icon name='cancel'/>Cancel</Button>
                    </Form.Group>
                    <Message error><Icon name='ban'/> {formErrorMessage}</Message>
                </Form>
            </Segment>
        );
    }
}

export {CreateItemFormContainer as CreateItemForm};