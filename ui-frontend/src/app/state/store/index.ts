import { addLocaleData } from 'react-intl';
import * as enLocaleData from 'react-intl/locale-data/en';
import { applyMiddleware, createStore, Middleware } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import logger from 'redux-logger';
import thunk from 'redux-thunk';

import { RootState } from '../../models';
import { rootReducer } from '../reducers';
import { initialRootState } from './initial-state';

addLocaleData([
    ...enLocaleData
]);

const middleware: Middleware[] = [];
middleware.push(thunk);
middleware.push(logger);

const storeEnhancer = composeWithDevTools(applyMiddleware(...middleware));

export const store = createStore<RootState, any, any, any>(rootReducer, initialRootState, storeEnhancer);
