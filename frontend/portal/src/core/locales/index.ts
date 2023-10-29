import en from './translations/en.json';
import no from './translations/no.json';
import {CookieSetOptions} from "universal-cookie";

export const defaultLocale = 'en';
export const userLocaleCookieName = 'acntech_user_locale';
export const userLocaleCookieOptions: CookieSetOptions = {path: '/', sameSite: 'strict'};

const messages = {
    en, no
}

export const supportedLocales: SupportedLocale[] = ['en', 'no'];

export type SupportedLocale = 'en' | 'no';

export const getLocaleMessages = (locale: SupportedLocale): Record<string, string> => {
    return messages[locale]
}
