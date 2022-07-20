import React, {FC, ReactElement, Suspense, useEffect} from 'react';
import {HashRouter as Router} from 'react-router-dom';
import {useIntl} from 'react-intl'
import {updateIntl} from 'react-intl-redux'
import {useDispatch} from 'react-redux';
import {Helmet} from 'react-helmet';
import {useCookies} from 'react-cookie';
import {HeaderMenuFragment} from './fragments';
import {Routes} from './routes';
import {defaultLocale, getIntlState, userLocaleCookieName, userLocaleCookieOptions} from './core/locales';
import store from './state/store';

import 'semantic-ui-css/semantic.min.css';
import './app.css';

const Loader = () => <p>Loading...</p>;

export const App: FC = (): ReactElement => {

    const dispatch = useDispatch();
    const {formatMessage: t} = useIntl();
    const [cookies, setCookie] = useCookies([userLocaleCookieName]);

    useEffect(() => {
        const userLocaleCookie = cookies[userLocaleCookieName];
        if (userLocaleCookie) {
            const updatedIntlState = getIntlState(userLocaleCookie);
            store.dispatch(updateIntl(updatedIntlState));
        } else {
            setCookie(userLocaleCookieName, defaultLocale, userLocaleCookieOptions);
        }
    }, [cookies]);

    useEffect(() => {
        //dispatch(getSecurityContext());
    }, [dispatch]);

    return (
        <>
            <Helmet>
                <meta charSet="utf-8"/>
                <title>{t({id: 'site.title'})}</title>
                <meta name="description" content={t({id: 'site.description'})}/>
            </Helmet>
            <Router>
                <HeaderMenuFragment/>
                <Suspense fallback={<Loader/>}>
                    <Routes/>
                </Suspense>
            </Router>
        </>
    );
};
