import React, {FC, ReactElement, SyntheticEvent, useCallback} from 'react'
import {Link} from 'react-router-dom'
import {useSelector} from 'react-redux';
import {FormattedMessage, useIntl} from 'react-intl'
import {useCookies} from 'react-cookie';
import {Container, Dropdown, DropdownProps, Menu} from 'semantic-ui-react'

import {RootState} from '../state/reducer';
import {
    getLocaleOption,
    getSupportedLocaleOptions,
    LocaleOption,
    SupportedLocale,
    userLocaleCookieName,
    userLocaleCookieOptions
} from '../core/locales';
import {setLocale} from '../core/locales/state';
import {SecurityContext} from '../types';


export const HeaderMenuFragment: FC = (): ReactElement => {

    const {formatMessage: t} = useIntl();
    const [cookies, setCookie] = useCookies([userLocaleCookieName]);

    const {intl: intlState} = useSelector((state: RootState) => state);
    const {securityContext: securityContextState} = useSelector((state: RootState) => state);

    const onLocaleChange = useCallback((event: SyntheticEvent<HTMLElement>, props: DropdownProps) => {
        const {value: locale} = props;
        if (!!locale) {
            setCookie(userLocaleCookieName, locale, userLocaleCookieOptions);
            setLocale(locale as SupportedLocale);
        }
    }, []);

    interface LocaleMenuItemProps {
        selectedLocale: LocaleOption;
        supportedLocales: LocaleOption[]
    }

    const LocaleMenuItem = (props: LocaleMenuItemProps): ReactElement => {
        const {selectedLocale, supportedLocales} = props;
        const filteredLocales = supportedLocales
            .filter(locale => locale.key !== selectedLocale.key);

        return (
            <Dropdown labeled item
                      icon='flag'
                      text={selectedLocale.text}
                      options={filteredLocales}
                      onChange={onLocaleChange}/>
        );
    };

    interface ProfileMenuItemProps {
        securityContext?: SecurityContext;
    }

    const ProfileMenuItem = (props: ProfileMenuItemProps): ReactElement => {
        const {securityContext} = props;

        if (securityContext) {
            const {userContext} = securityContext;
            const {username, firstName, lastName} = userContext;
            return (
                <Dropdown simple item text={t({id: 'main-menu.profile.menu-item'})}>
                    <Dropdown.Menu>
                        <Dropdown.Item disabled>{`${firstName} ${lastName} (${username})`}</Dropdown.Item>
                        <Dropdown.Divider/>
                        <Dropdown.Item>
                        </Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            );
        } else {
            return (
                <Dropdown simple item disabled text={t({id: 'main-menu.menu-item.profile'})}/>
            );
        }
    };

    return (
        <Menu borderless>
            <Container>
                <Menu.Menu position='left'>
                    <Menu.Item header as={Link} to="/"><FormattedMessage id="main-menu.title"/></Menu.Item>
                </Menu.Menu>
                <Menu.Menu position='right'>
                    <LocaleMenuItem
                        selectedLocale={getLocaleOption(intlState.locale as SupportedLocale)}
                        supportedLocales={getSupportedLocaleOptions()}/>
                    <ProfileMenuItem securityContext={securityContextState.data}/>
                </Menu.Menu>
            </Container>
        </Menu>
    );
};
