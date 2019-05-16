import * as React from 'react';
import { Component, ReactNode } from 'react';
import { FormattedMessage, injectIntl } from 'react-intl';
import { Button, Form, Icon, InputOnChangeData, Message, Segment, TextAreaProps } from 'semantic-ui-react';
import { FormData, FormElementData } from '../../models/types';
import InjectedIntlProps = ReactIntl.InjectedIntlProps;

interface ComponentParamProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormInputNameChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    onFormTextAreaDescriptionChange: (event: React.SyntheticEvent<HTMLTextAreaElement>, data: TextAreaProps) => void;
    formData: CreateOrderFormData;
}

type ComponentProps = ComponentParamProps & InjectedIntlProps;

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

class CreateOrderFormComponent extends Component<ComponentProps> {

    public render(): ReactNode {
        const {
            onCancelButtonClick,
            onFormSubmit,
            onFormInputNameChange,
            onFormTextAreaDescriptionChange,
            formData,
            intl
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
        const orderNameText = intl.formatMessage({id: 'label.order-name.text'});
        const orderNamePlaceholderText = intl.formatMessage({id: 'form.placeholder.order-name.text'});
        const orderDescriptionText = intl.formatMessage({id: 'label.order-description.text'});
        const orderDescriptionPlaceholderText = intl.formatMessage({id: 'form.placeholder.order-description.text'});

        return (
            <Segment basic>
                <Form onSubmit={onFormSubmit} error={formError}>
                    <Form.Group>
                        <Form.Input error={formNameError}
                                    width={10}
                                    label={orderNameText}
                                    placeholder={orderNamePlaceholderText}
                                    value={formNameValue}
                                    onChange={onFormInputNameChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.TextArea width={10}
                                       label={orderDescriptionText}
                                       placeholder={orderDescriptionPlaceholderText}
                                       value={formDescriptionValue}
                                       onChange={onFormTextAreaDescriptionChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Button primary size="tiny">
                            <Icon name="dolly" /><FormattedMessage id="button.create-order.text" />
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

const IntlCreateOrderFormComponent = injectIntl(CreateOrderFormComponent);

export { IntlCreateOrderFormComponent as CreateOrderForm };