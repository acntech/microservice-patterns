import client from '../../core/client';

import { Config, GetConfigActionType, GetConfigErrorAction, GetConfigLoadingAction, GetConfigSuccessAction } from '../../models';
import { showErrorNotification } from '../actions';

const getConfigLoading = (): GetConfigLoadingAction => ({type: GetConfigActionType.LOADING, loading: true});
const getConfigSuccess = (payload: Config): GetConfigSuccessAction => ({type: GetConfigActionType.SUCCESS, payload});
const getConfigError = (error: any): GetConfigErrorAction => ({type: GetConfigActionType.ERROR, error});

const rootPath = 'client/config';

export function getConfig() {
    return (dispatch) => {
        dispatch(getConfigLoading());
        return client.get(rootPath)
            .then((response) => {
                const {body} = response;
                return dispatch(getConfigSuccess(body));
            })
            .catch((error) => {
                const {message} = error.response && error.response.body;
                dispatch(showErrorNotification({id: 'action.get-config-error.title.text'}, message, true));
                return dispatch(getConfigError(error));
            });
    };
}
