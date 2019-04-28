import * as React from 'react';
import {Component, ReactNode} from 'react';
import {Button, Container, Form, Icon, InputOnChangeData, Message, Segment, TextAreaProps} from 'semantic-ui-react';
import {PrimaryHeader, SecondaryHeader} from '../../components';
import {FormData, FormElementData} from "../../models/types";

interface ComponentProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputNameChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    onFormTextAreaDescriptionChange: (event: React.SyntheticEvent<HTMLTextAreaElement>, data: TextAreaProps) => void;
    formData: CreateOrderFormData;
}

export interface CreateOrderFormData extends FormData {
    formInputName: FormElementData;
    formTextAreaDescription?: FormElementData;
}

const initialCreateOrderFormElementData: FormElementData = {
    formElementError: false,
    formElementValue: ''
};

export const initialCreateOrderFormData: CreateOrderFormData = {
    formSubmitted: false,
    formError: false,
    formInputName: initialCreateOrderFormElementData,
    formTextAreaDescription: initialCreateOrderFormElementData
};

class CreateOrderFormContainer extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            onCancelButtonClick,
            onFormSubmit,
            onFormInputNameChange,
            onFormTextAreaDescriptionChange,
            formData
        } = this.props;
        const {
            formError,
            formErrorMessage,
            formInputName,
            formTextAreaDescription
        } = formData;
        const {
            formElementError: formNameError,
            formElementValue: formNameValue
        } = formInputName;
        const {
            formElementValue: formDescriptionValue
        } = formTextAreaDescription || {formElementValue: undefined};

        return (
            <Container>
                <PrimaryHeader/>
                <SecondaryHeader/>
                <Segment basic>
                    <Form onSubmit={onFormSubmit} error={formError}>
                        <Form.Group>
                            <Form.Input
                                error={formNameError}
                                width={10}
                                label='Order Name'
                                placeholder='Enter order name...'
                                value={formNameValue}
                                onChange={onFormInputNameChange}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.TextArea
                                width={10}
                                label='Order Description'
                                placeholder='Enter order description...'
                                value={formDescriptionValue}
                                onChange={onFormTextAreaDescriptionChange}/>
                        </Form.Group>
                        <Form.Group>
                            <Form.Button
                                primary size='tiny'><Icon name='dolly'/>Create Order</Form.Button>
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

export {CreateOrderFormContainer as CreateOrderForm};