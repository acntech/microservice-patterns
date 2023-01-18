import {SupportedLocale} from "../core/locales";

export interface AppSettings {
    locale: SupportedLocale
    setLocale: (newLocale: SupportedLocale) => void
}