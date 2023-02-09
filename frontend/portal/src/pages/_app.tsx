import type {AppProps} from "next/app";
import {createContext, FC, ReactElement, ReactNode, useEffect, useState} from "react";
import {IntlProvider} from "react-intl";
import {CookiesProvider, useCookies} from "react-cookie";
import {Container} from "semantic-ui-react";
import {
    defaultLocale,
    getLocaleMessages,
    SupportedLocale,
    userLocaleCookieName,
    userLocaleCookieOptions
} from "../core/locales";
import {AppSettings, ClientError, ClientResponse, ErrorPayload, PageState, SessionContext} from "../types";
import {HeaderMenuFragment} from "../fragments";
import "semantic-ui-css/semantic.min.css";
import "../styles/globals.css";
import {RestConsumer} from "../core/consumer";

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

    const [pageState, setPageState] = useState<PageState<SessionContext>>({status: 'LOADING'});

    useEffect(() => {
        RestConsumer.getSessionContext(
            (response: ClientResponse<SessionContext>) => setPageState({status: 'SUCCESS', data: response}),
            (error: ClientError<ErrorPayload>) => setPageState({status: 'FAILED', error: error.response}));
    }, []);

    return (
        <CookiesProvider>
            <LocaleAwareApp>
                <Container>
                    <HeaderMenuFragment sessionContext={pageState.data?.body}/>
                    <Component {...pageProps} />
                </Container>
            </LocaleAwareApp>
        </CookiesProvider>
    )
}

export default App;
