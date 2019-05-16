import {
    ActionType,
    EntityType,
    FindProductsAction,
    FindProductsActionType,
    GetProductAction,
    GetProductActionType,
    Product,
    ProductAction,
    ProductState
} from '../../models';
import {initialProductState} from '../store/initial-state';

export const reducer = (state: ProductState = initialProductState, action: ProductAction): ProductState => {
    switch (action.type) {
        case GetProductActionType.LOADING:
        case GetProductActionType.SUCCESS:
        case GetProductActionType.ERROR:
            return get(state, action);
        case FindProductsActionType.LOADING:
        case FindProductsActionType.SUCCESS:
        case FindProductsActionType.ERROR:
            return find(state, action);
        default:
            return state;
    }
};

const get = (state: ProductState = initialProductState, action: GetProductAction): ProductState => {
    switch (action.type) {
        case GetProductActionType.LOADING: {
            const {products} = state;
            const {loading} = action;
            return {...initialProductState, products: products, loading: loading};
        }

        case GetProductActionType.SUCCESS: {
            let {products} = state;
            const {payload} = action;

            if (payload) {
                products = replaceOrAppend(products, payload);
            }

            return {...initialProductState, products: products};
        }

        case GetProductActionType.ERROR: {
            const {products} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.PRODUCTS, actionType: ActionType.FIND};
            return {...initialProductState, products: products, error: error};
        }

        default: {
            return state;
        }
    }
};

const find = (state: ProductState = initialProductState, action: FindProductsAction): ProductState => {
    switch (action.type) {
        case FindProductsActionType.LOADING: {
            const {products} = state;
            const {loading} = action;
            return {...initialProductState, products: products, loading: loading};
        }

        case FindProductsActionType.SUCCESS: {
            let {products} = state;
            const {payload} = action;

            if (payload) {
                payload.forEach(product => {
                    products = replaceOrAppend(products, product);
                });
            }

            return {...initialProductState, products: products};
        }

        case FindProductsActionType.ERROR: {
            const {products} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.PRODUCTS, actionType: ActionType.FIND};
            return {...initialProductState, products: products, error: error};
        }

        default: {
            return state;
        }
    }
};

const replaceOrAppend = (products: Product[], product: Product) => {
    const index = products.map(p => p.productId).indexOf(product.productId);

    if (index !== -1) {
        products[index] = product;
    } else {
        products = products.concat(product);
    }

    return products;
};