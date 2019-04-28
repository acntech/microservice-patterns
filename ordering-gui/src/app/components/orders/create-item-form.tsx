import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, Container, Form, Icon, InputOnChangeData, Message, Segment} from 'semantic-ui-react';
import {PrimaryHeader, SecondaryHeader} from '../../components';
import {FormData, FormElementData} from "../../models";


interface ComponentProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputProductIdChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    onFormInputQuantityChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    formData: CreateItemFormData;
}

export interface CreateItemFormData extends FormData {
    formInputProductId: FormElementData;
    formInputQuantity: FormElementData;
}

const initialCreateItemFormElementData: FormElementData = {
    formElementError: false,
    formElementValue: ''
};

export const initialCreateItemFormData: CreateItemFormData = {
    formSubmitted: false,
    formError: false,
    formInputProductId: initialCreateItemFormElementData,
    formInputQuantity: initialCreateItemFormElementData
};

class CreateItemFormContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            onCancelButtonClick,
            onFormSubmit,
            onFormInputProductIdChange,
            onFormInputQuantityChange,
            formData
        } = this.props;
        const {
            formError,
            formErrorMessage,
            formInputProductId,
            formInputQuantity
        } = formData;
        const {
            formElementError: formProductIdError,
            formElementValue: formProductIdValue
        } = formInputProductId;
        const {
            formElementError: formQuantityError,
            formElementValue: formQuantityValue
        } = formInputQuantity;

        return (
            <Container>
                <PrimaryHeader/>
                <SecondaryHeader/>
                <Segment basic>
                    <Form onSubmit={onFormSubmit} error={formError}>
                        <Form.Group>
                            <Form.Input
                                error={formProductIdError}
                                width={10}
                                label='Product ID'
                                placeholder='Enter product ID...'
                                value={formProductIdValue}
                                onChange={onFormInputProductIdChange}/>
                        </Form.Group>
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
            </Container>
        );
    }
}

export {CreateItemFormContainer as CreateItemForm};