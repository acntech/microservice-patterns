import { ActionType, CreateItemAction, CreateItemActionType, DeleteItemAction, DeleteItemActionType, EntityType, GetItemAction, GetItemActionType, Item, ItemAction, ItemState } from '../../models';
import { initialItemState } from '../store/initial-state';

export const reducer = (state: ItemState = initialItemState, action: ItemAction): ItemState => {
    switch (action.type) {
        case GetItemActionType.LOADING:
        case GetItemActionType.SUCCESS:
        case GetItemActionType.ERROR:
            return get(state, action);
        case CreateItemActionType.LOADING:
        case CreateItemActionType.SUCCESS:
        case CreateItemActionType.ERROR:
            return create(state, action);
        case DeleteItemActionType.LOADING:
        case DeleteItemActionType.SUCCESS:
        case DeleteItemActionType.ERROR:
            return remove(state, action);
        default:
            return state;
    }
};

const get = (state: ItemState = initialItemState, action: GetItemAction): ItemState => {
    let {items} = state;

    switch (action.type) {
        case GetItemActionType.LOADING: {
            const {loading} = action;
            return {...initialItemState, items, loading};
        }

        case GetItemActionType.SUCCESS: {
            const {payload} = action;

            if (payload) {
                items = replaceOrAppend(items, payload);
            }

            return {...initialItemState, items};
        }

        case GetItemActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ORDERS, actionType: ActionType.GET};
            return {...initialItemState, items, error};
        }

        default: {
            return state;
        }
    }
};

const create = (state: ItemState = initialItemState, action: CreateItemAction): ItemState => {
    const {items} = state;

    switch (action.type) {
        case CreateItemActionType.LOADING: {
            const {loading} = action;
            return {...initialItemState, items, loading};
        }

        case CreateItemActionType.SUCCESS: {
            const {headers} = action;
            let modified;

            if (headers) {
                const {location} = headers;
                const orderId = location.split('items/')[1];
                modified = {id: orderId, entityType: EntityType.ITEMS, actionType: ActionType.CREATE};
            }

            return {...initialItemState, items, modified};
        }

        case CreateItemActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ITEMS, actionType: ActionType.CREATE};
            return {...initialItemState, items, error};
        }

        default: {
            return state;
        }
    }
};

const remove = (state: ItemState = initialItemState, action: DeleteItemAction): ItemState => {
    const {items} = state;

    switch (action.type) {
        case DeleteItemActionType.LOADING: {
            const {loading} = action;
            return {...initialItemState, items, loading};
        }

        case DeleteItemActionType.SUCCESS: {
            const {itemId} = action;
            const modified = {id: itemId, entityType: EntityType.ITEMS, actionType: ActionType.DELETE};
            return {...initialItemState, items, modified};
        }

        case DeleteItemActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ITEMS, actionType: ActionType.DELETE};
            return {...initialItemState, items, error};
        }

        default: {
            return state;
        }
    }
};

const replaceOrAppend = (items: Item[], item: Item) => {
    const index = items.map(i => i.itemId).indexOf(item.itemId);

    if (index !== -1) {
        items[index] = item;
    } else {
        items = items.concat(item);
    }

    return items;
};
