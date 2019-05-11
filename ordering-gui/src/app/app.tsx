import * as React from 'react';
import { Component, ReactNode } from 'react';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import { RootContainer } from './containers';

import { ErrorHandlerProvider } from './providers';
import { store } from './state/store';

class App extends Component<{}> {

    public render(): ReactNode {
        const {intl} = store.getState();
        const {locale, messages} = intl;

        return (
            <Provider store={store}>
                <IntlProvider key={locale} locale={locale} messages={messages}>
                    <ErrorHandlerProvider>
                        <RootContainer />
                    </ErrorHandlerProvider>
                </IntlProvider>
            </Provider>
        );
    }
}

export { App };