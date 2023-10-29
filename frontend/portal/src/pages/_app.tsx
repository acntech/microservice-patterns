import type {AppProps} from "next/app";
import React, {createContext, FC, ReactElement, ReactNode, useEffect, useReducer, useState} from "react";
import {IntlProvider} from "react-intl";
import {CookiesProvider, useCookies} from "react-cookie";
import {config} from "@fortawesome/fontawesome-svg-core";
import {
    defaultLocale,
    getLocaleMessages,
    SupportedLocale,
    userLocaleCookieName,
    userLocaleCookieOptions
} from "../core/locales";
import {AppSettings, ClientError, ClientResponse, ErrorPayload, SessionContext, Status} from "../types";
import {HeaderMenuFragment} from "../fragments";
import {RestConsumer} from "../core/consumer";
import {sessionReducer} from "../state/reducers";

import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-svg-core/styles.css";
import "../styles/globals.css";

config.autoAddCss = false;

interface LocaleAwareAppProps {
    children: ReactNode
}

const defaultSettings: AppSettings = {
    locale: defaultLocale,
    setLocale: (newLocale: SupportedLocale) => {
    }
}

export const SettingsContext = createContext(defaultSettings);

const LocaleAwareApp: FC<LocaleAwareAppProps> = (props: LocaleAwareAppProps): ReactElement => {

    const [cookies, setCookie] = useCookies([userLocaleCookieName]);
    let initialLocale = cookies[userLocaleCookieName];
    if (!initialLocale) {
        initialLocale = defaultLocale
        setCookie(userLocaleCookieName, initialLocale, userLocaleCookieOptions);
    }

    const [locale, setLocale] = useState<SupportedLocale>(initialLocale);
    const messages = getLocaleMessages(locale)

    const setNewLocale = (newLocale: SupportedLocale) => {
        setCookie(userLocaleCookieName, newLocale, userLocaleCookieOptions);
        setLocale(newLocale);
    }

    const updatedSettings: AppSettings = {
        locale,
        setLocale: setNewLocale
    }

    return (
        <SettingsContext.Provider value={updatedSettings}>
            <IntlProvider locale={locale} messages={messages}>
                {props.children}
            </IntlProvider>
        </SettingsContext.Provider>
    )
}

const App: FC<AppProps> = ({Component, pageProps}: AppProps): ReactElement => {

    const [sessionState, sessionDispatch] = useReducer(sessionReducer, {status: Status.LOADING});

    useEffect(() => {
        RestConsumer.getSessionContext(
            (response: ClientResponse<SessionContext>) => sessionDispatch({status: Status.SUCCESS, data: response}),
            (error: ClientError<ErrorPayload>) => sessionDispatch({status: Status.FAILED, error: error.response}));
    }, []);

    return (
        <CookiesProvider>
            <LocaleAwareApp>
                <HeaderMenuFragment sessionContext={sessionState.data}/>
                <Component {...pageProps} />
            </LocaleAwareApp>
        </CookiesProvider>
    )
}

export default App;
