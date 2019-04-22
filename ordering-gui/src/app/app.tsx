import * as React from 'react';
import { Component, ReactNode } from 'react';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { RootContainer } from './containers';
import { ErrorHandlerProvider } from './providers';

import { store } from './state/store';

class App extends Component {

    constructor(props) {
        super(props);
    }

    public render(): ReactNode {
        return (
            <Provider store={store}>
                <IntlProvider locale='en'>
                        <ErrorHandlerProvider>
                            <RootContainer />
                        </ErrorHandlerProvider>
                </IntlProvider>
            </Provider>
        );
    }
}

export { App };