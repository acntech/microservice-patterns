import * as React from 'react';
import { Component, ReactNode } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import { Action } from 'redux';
import { ThunkDispatch } from 'redux-thunk';
import { Container } from 'semantic-ui-react';
import { LoadingIndicator, NotFoundError, PrimaryHeader, SecondaryHeader } from '../../components';
import { ShowItem } from '../../components/orders/show-item';

import { Item, ItemState, Product, ProductState, RootState } from '../../models';
import { deleteItem, getItem, getProduct } from '../../state/actions';

interface RouteProps {
    match: any;
}

interface ComponentStateProps {
    productState: ProductState;
    itemState: ItemState;
}

interface ComponentDispatchProps {
    getItem: (itemId: string) => Promise<any>;
    getProduct: (productId: string) => Promise<any>;
    deleteItem: (itemId: string) => Promise<any>;
}

type ComponentProps = ComponentDispatchProps & ComponentStateProps & RouteProps;

interface ComponentState {
    back: boolean;
    item?: Item;
    product?: Product;
}

const initialState: ComponentState = {
    back: false
};

class ItemContainer extends Component<ComponentProps, ComponentState> {

    constructor(props: ComponentProps) {
        super(props);
        this.state = initialState;
    }

    public componentDidMount(): void {
        const {itemId} = this.props.match.params;
        this.props.getItem(itemId);
    }

    public componentDidUpdate(): void {
        const {itemId} = this.props.match.params;
        const {loading: itemLoading, items} = this.props.itemState;
        const {loading: productLoading, products} = this.props.productState;
        const {item, product} = this.state;

        if (!itemLoading && !item) {
            const selectedItem = items.find(i => i.itemId === itemId);
            if (selectedItem) {
                this.setState({item: selectedItem});
            }
        }

        if (item && !productLoading && !product) {
            const {productId} = item;
            const selectedProduct = products.find(p => p.productId === productId);
            if (selectedProduct) {
                this.setState({product: selectedProduct});
            } else {
                this.props.getProduct(productId);
            }
        }
    }

    public render(): ReactNode {
        const {orderId, itemId} = this.props.match.params;
        const {loading: itemLoading} = this.props.itemState;
        const {loading: productLoading} = this.props.productState;
        const {back, item, product} = this.state;

        if (back) {
            return <Redirect to={`/orders/${orderId}`} />;
        } else if (itemLoading || productLoading) {
            return <LoadingIndicator />;
        } else if (!item) {
            return (
                <Container className="error error-not-found">
                    <PrimaryHeader />
                    <NotFoundError
                        icon="warning sign"
                        header={{id: 'error.item-not-found.header.text'}}
                        content={{id: 'error.item-not-found.content.text', values: {itemId}}} />
                </Container>
            );
        } else if (!product) {
            return (
                <Container className="error error-not-found">
                    <PrimaryHeader />
                    <NotFoundError
                        icon="warning sign"
                        header={{id: 'error.product-not-found.header.text'}}
                        content={{id: 'error.product-not-found.content.text', values: {itemId}}} />
                </Container>
            );
        } else {
            return (
                <Container>
                    <PrimaryHeader />
                    <SecondaryHeader />
                    <ShowItem item={item}
                        product={product}
                        onBackButtonClick={this.onBackButtonClick}
                        onDeleteButtonClick={this.onDeleteButtonClick}
                        onRefreshButtonClick={this.onRefreshButtonClick} />
                </Container>
            );
        }
    }

    private onBackButtonClick = (): void => {
        this.setState({
            back: true
        });
    };

    private onDeleteButtonClick = (): void => {
        const {itemId} = this.props.match.params;
        this.props.deleteItem(itemId);
    };

    private onRefreshButtonClick = (): void => {
        const {itemId} = this.props.match.params;
        this.setState({
            ...initialState,
            item: undefined
        });
        this.props.getItem(itemId);
    };
}

const mapStateToProps = (state: RootState): ComponentStateProps => ({
    productState: state.productState,
    itemState: state.itemState
});

const mapDispatchToProps = (dispatch: ThunkDispatch<RootState, void, Action>): ComponentDispatchProps => ({
    getItem: (itemId: string) => dispatch(getItem(itemId)),
    getProduct: (productId: string) => dispatch(getProduct(productId)),
    deleteItem: (itemId: string) => dispatch(deleteItem(itemId))
});

const ConnectedItemContainer = connect(mapStateToProps, mapDispatchToProps)(ItemContainer);

export { ConnectedItemContainer as ItemContainer };