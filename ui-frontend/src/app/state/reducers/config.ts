import { ConfigAction, ConfigState } from '../../models';
import { initialConfigState } from '../store/initial-state';

export const reducer = (state: ConfigState = initialConfigState, action: ConfigAction): ConfigState => {
    return state;
};
