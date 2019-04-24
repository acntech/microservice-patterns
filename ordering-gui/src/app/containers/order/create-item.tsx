import * as React from 'react';
import { ChangeEventHandler, Component, FunctionComponent, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Button, Container, Form, Icon, InputOnChangeData, Message, Segment } from 'semantic-ui-react';
import { LoadingIndicator, PrimaryHeader, SecondaryHeader } from '../../components';

import { ActionType, CreateItem, ItemState, RootState } from '../../models';
import { createItem } from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    itemState: ItemState;
}

interface ComponentDispatchProps {
    createItem: (orderId: string, create: CreateItem) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface FormInputData {
    formError: boolean;
    formValue: string;
}

interface FormData {
    formError: boolean;
    formErrorMessage?: string;
    formInputProductId: FormInputData;
    formInputQuantity: FormInputData;
}

const initialFormInputData: FormInputData = {
    formError: false,
    formValue: ''
};

const initialFormData: FormData = {
    formError: false,
    formInputProductId: initialFormInputData,
    formInputQuantity: initialFormInputData
};

interface ComponentState {
    cancel: boolean;
    formData: FormData;
}

const initialState: ComponentState = {
    cancel: false,
    formData: initialFormData
};

class CreateItemContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public render(): ReactNode {
        const {orderId} = this.props.match.params;
        const {loading, modified} = this.props.itemState;
        const {cancel, formData} = this.state;

        if (cancel) {
            return <Redirect to='/' />;
        } else if (loading) {
            return <LoadingIndicator />;
        } else if (modified && modified.actionType === ActionType.CREATE) {
            return <Redirect to={`/orders/${orderId}`} />;
        } else {
            return <CreateItemFragment
                onCancelButtonClick={this.onCancelButtonClick}
                onFormSubmit={this.onFormSubmit}
                onFormProductIdChange={this.onFormProductIdChange}
                onFormQuantityChange={this.onFormQuantityChange}
                formData={formData} />;
        }
    }

    private onFormSubmit = () => {
        const {orderId} = this.props.match.params;
        const {formData} = this.state;
        const {formInputProductId, formInputQuantity} = formData;
        const {formValue: productId} = formInputProductId;
        const {formValue: quantity} = formInputQuantity;

        if (this.formInputProductIdIsValid(formData) && this.formInputQuantityIsValid(formData)) {
            this.props.createItem(
                orderId, {
                    productId: productId,
                    quantity: parseInt(quantity, 10)
                });
        }
    };

    private formInputProductIdIsValid = (formData: FormData): boolean => {
        const {formInputProductId} = formData;
        const {formValue: productId} = formInputProductId;

        if (!productId || productId.length !== 36) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Product ID must be an UUID of 36 characters',
                    formInputProductId: {
                        ...formInputProductId,
                        formError: true
                    }
                }
            });

            return false;
        } else {
            return true;
        }
    };

    private formInputQuantityIsValid = (formData: FormData): boolean => {
        const {formInputQuantity} = formData;
        const {formValue: quantity} = formInputQuantity;

        const quantityNumber = parseInt(quantity, 10);
        if (isNaN(quantityNumber) || quantityNumber < 1) {
            this.setState({
                formData: {
                    ...formData,
                    formError: true,
                    formErrorMessage: 'Quantity must be a positive number',
                    formInputQuantity: {
                        ...formInputQuantity,
                        formError: true
                    }
                }
            });

            return false;
        } else {
            return true;
        }
    };

    private onFormProductIdChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formInputProductId} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formInputProductId: {
                    ...formInputProductId,
                    formError: false,
                    formValue: value
                }
            }
        });
    };

    private onFormQuantityChange: ChangeEventHandler<HTMLInputElement> = (event) => {
        const {value} = event.currentTarget;
        const {formData} = this.state;
        const {formInputQuantity} = formData;

        this.setState({
            formData: {
                ...formData,
                formError: false,
                formInputQuantity: {
                    ...formInputQuantity,
                    formError: false,
                    formValue: value
                }
            }
        });
    };

    private onCancelButtonClick = () => {
        this.setState({cancel: true});
    };
}

interface CreateItemFragmentProps {
    onCancelButtonClick: () => void;
    onFormSubmit: () => void;
    onFormProductIdChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    onFormQuantityChange: (event: React.SyntheticEvent<HTMLInputElement>, data: InputOnChangeData) => void;
    formData: FormData;
}

const CreateItemFragment: FunctionComponent<CreateItemFragmentProps> = (props) => {
    const {
        onCancelButtonClick,
        onFormSubmit,
        onFormProductIdChange,
        onFormQuantityChange,
        formData
    } = props;
    const {
        formError,
        formErrorMessage,
        formInputProductId,
        formInputQuantity
    } = formData;
    const {
        formError: formProductIdError,
        formValue: formProductIdValue
    } = formInputProductId;
    const {
        formError: formQuantityError,
        formValue: formQuantityValue
    } = formInputQuantity;

    return (
        <Container>
            <PrimaryHeader />
            <SecondaryHeader />
            <Segment basic>
                <Form onSubmit={onFormSubmit} error={formError}>
                    <Form.Group>
                        <Form.Input
                            error={formProductIdError}
                            width={10}
                            label='Product ID'
                            placeholder='Enter product ID...'
                            value={formProductIdValue}
                            onChange={onFormProductIdChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Input
                            error={formQuantityError}
                            width={10}
                            label='Quantity'
                            placeholder='Enter quantity...'
                            value={formQuantityValue}
                            onChange={onFormQuantityChange} />
                    </Form.Group>
                    <Form.Group>
                        <Form.Button
                            primary size='tiny'><Icon name='dolly' />Add Item</Form.Button>
                        <Button
                            secondary size='tiny'
                            onClick={onCancelButtonClick}><Icon name='cancel' />Cancel</Button>
                    </Form.Group>
                    <Message error><Icon name='ban' /> {formErrorMessage}</Message>
                </Form>
            </Segment>
        </Container>
    );
};

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    itemState: state.itemState
});

const mapDispatchToProps = (dispatch): ComponentDispatchProps => ({
    createItem: (orderId: string, create: CreateItem) => dispatch(createItem(orderId, create))
});

const ConnectedCreateItemContainer = connect(mapStateToProps, mapDispatchToProps)(CreateItemContainer);

export { ConnectedCreateItemContainer as CreateItemContainer };