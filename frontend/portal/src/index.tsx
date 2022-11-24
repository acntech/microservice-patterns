import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-intl-redux';
import { CookiesProvider } from 'react-cookie';
import { App } from './app';
import { ErrorBoundaryProvider } from './providers';
import store from './state/store';

if ((module as any).hot) {
  (module as any).hot.accept();
}

ReactDOM.render(
  <ErrorBoundaryProvider>
    <Provider store={store}>
      <CookiesProvider>
        <App />
      </CookiesProvider>
    </Provider>
  </ErrorBoundaryProvider>,
  document.getElementById('root')
);