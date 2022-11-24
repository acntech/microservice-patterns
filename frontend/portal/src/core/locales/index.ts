import {IntlState} from 'react-intl-redux';

import en from './translations/en.json';
import no from './translations/no.json';
import {CookieSetOptions} from "universal-cookie";

export const defaultLocale = 'en';
export const userLocaleCookieName = 'acntech_user_locale';
export const userLocaleCookieOptions: CookieSetOptions = {path: '/', sameSite: 'strict'};
export const defaultTimeLocale = 'no-NO';

const messages = {
    en, no
}

export const supportedLocales: SupportedLocale[] = ['en', 'no'];

export type SupportedLocale = 'en' | 'no';

export const getIntlState = (locale: SupportedLocale): IntlState => {
    return {
        locale,
        messages: messages[locale]
    };
};

export interface LocaleOption {
    key: SupportedLocale;
    value: SupportedLocale;
    text: string;
}

export const getLocaleOption = (locale: SupportedLocale): LocaleOption => {
    return {
        key: locale,
        value: locale,
        text: locale.toUpperCase()
    };
};

export const getSupportedLocaleOptions = (): LocaleOption[] => {
    return supportedLocales.map(getLocaleOption)
};
