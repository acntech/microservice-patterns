import {
    ActionType,
    CreateItemAction,
    CreateItemActionType,
    DeleteItemAction,
    DeleteItemActionType,
    EntityType,
    ItemAction,
    ItemState
} from '../../models';
import {initialItemState} from '../store/initial-state';

export const reducer = (state: ItemState = initialItemState, action: ItemAction): ItemState => {
    switch (action.type) {
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

const create = (state: ItemState = initialItemState, action: CreateItemAction): ItemState => {
    switch (action.type) {
        case CreateItemActionType.LOADING: {
            const {loading} = action;
            return {...initialItemState, loading: loading};
        }

        case CreateItemActionType.SUCCESS: {
            const {headers} = action;
            let modified;

            if (headers) {
                const {location} = headers;
                const orderId = location.split('items/')[1];
                modified = {id: orderId, entityType: EntityType.ITEMS, actionType: ActionType.CREATE};
            }

            return {...initialItemState, modified: modified};
        }

        case CreateItemActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ITEMS, actionType: ActionType.CREATE};
            return {...initialItemState, error: error};
        }

        default: {
            return state;
        }
    }
};

const remove = (state: ItemState = initialItemState, action: DeleteItemAction): ItemState => {
    switch (action.type) {
        case DeleteItemActionType.LOADING: {
            const {loading} = action;
            return {...initialItemState, loading: loading};
        }

        case DeleteItemActionType.SUCCESS: {
            const {orderId} = action;
            const modified = {id: orderId, entityType: EntityType.ITEMS, actionType: ActionType.DELETE};
            return {...initialItemState, modified: modified};
        }

        case DeleteItemActionType.ERROR: {
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.ITEMS, actionType: ActionType.DELETE};
            return {...initialItemState, error: error};
        }

        default: {
            return state;
        }
    }
};
