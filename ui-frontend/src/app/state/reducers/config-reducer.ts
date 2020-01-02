import { ActionType, ConfigAction, ConfigState, EntityType, GetConfigAction, GetConfigActionType } from '../../models';
import { INITIAL_CONFIG_STATE } from '../store/initial-state';

export const reducer = (state: ConfigState = INITIAL_CONFIG_STATE, action: ConfigAction): ConfigState => {
    switch (action.type) {
        case GetConfigActionType.LOADING:
        case GetConfigActionType.SUCCESS:
        case GetConfigActionType.ERROR:
            return get(state, action);
        default:
            return state;
    }
};

const get = (state: ConfigState, action: GetConfigAction): ConfigState => {
    switch (action.type) {
        case GetConfigActionType.LOADING: {
            const {config} = state;
            const {loading} = action;
            return {...state, loading: loading, config: config};
        }

        case GetConfigActionType.SUCCESS: {
            let {config} = state;
            const {payload} = action;

            config = {...config, ...payload};

            return {...state, loading: false, config: config};
        }

        case GetConfigActionType.ERROR: {
            const {config} = state;
            const {data} = action.error.response;
            const error = {...data, entityType: EntityType.CONFIG, actionType: ActionType.GET};
            return {...state, loading: false, error: error, config: config};
        }

        default: {
            return state;
        }
    }
};
