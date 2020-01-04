import client from '../../core/client';

import { Config, GetConfigActionType, GetConfigErrorAction, GetConfigLoadingAction, GetConfigSuccessAction } from '../../models';

const getConfigLoading = (): GetConfigLoadingAction => ({type: GetConfigActionType.LOADING, loading: true});
const getConfigSuccess = (payload: Config): GetConfigSuccessAction => ({type: GetConfigActionType.SUCCESS, payload});
const getConfigError = (error: any): GetConfigErrorAction => ({type: GetConfigActionType.ERROR, error});

const rootPath = 'client/config';

export function getConfig() {
    return (dispatch) => {
        dispatch(getConfigLoading());
        return client.get(rootPath)
            .then((response) => {
                return dispatch(getConfigSuccess(response.body));
            })
            .catch((error) => {
                return dispatch(getConfigError(error));
            });
    };
}
