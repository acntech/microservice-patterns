import { updateIntl } from 'react-intl-redux';

import store from '../../state/store';
import { getIntlState, SupportedLocale } from './';

export const setLocale = (locale: SupportedLocale) => {
    const updatedIntlState = getIntlState(locale);
    store.dispatch(updateIntl(updatedIntlState));
};