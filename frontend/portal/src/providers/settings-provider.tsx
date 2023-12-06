import {AppSettings} from "../types";
import {
    defaultLocale,
    getLocaleMessages,
    SupportedLocale,
    userLocaleCookieName,
    userLocaleCookieOptions
} from "../core/locales";
import React, {createContext, FC, ReactElement, ReactNode, useState} from "react";
import {useCookies} from "react-cookie";
import {IntlProvider} from "react-intl";

const defaultSettings: AppSettings = {
    locale: defaultLocale,
    setLocale: (newLocale: SupportedLocale) => {
    }
}

export const Settings = createContext(defaultSettings);

export interface SettingProviderProps {
    children: ReactNode
}

export const SettingProvider: FC<SettingProviderProps> = (props: SettingProviderProps): ReactElement => {

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
        <Settings.Provider value={updatedSettings}>
            <IntlProvider locale={locale} messages={messages}>
                {props.children}
            </IntlProvider>
        </Settings.Provider>
    )
};
