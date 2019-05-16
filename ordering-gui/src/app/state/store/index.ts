import { addLocaleData } from 'react-intl';
import * as enLocaleData from 'react-intl/locale-data/en';
import { applyMiddleware, createStore, Middleware } from 'redux';
import logger from 'redux-logger';
import thunk from 'redux-thunk';

import { RootState } from '../../models';
import { rootReducer } from '../reducers';
import { initialRootState } from './initial-state';

addLocaleData([
    ...enLocaleData
]);

const middlewares: Middleware[] = [];

middlewares.push(thunk);
if (process.env.NODE_ENV !== 'production') {
    middlewares.push(logger);

}

export const store = createStore<RootState>(rootReducer, initialRootState, applyMiddleware(...middlewares));
