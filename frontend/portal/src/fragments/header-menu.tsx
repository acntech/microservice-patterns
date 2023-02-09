import React, {FC, ReactElement, useContext} from 'react'
import {FormattedMessage, useIntl} from 'react-intl'
import Link from "next/link";
import {Dropdown, Image, Menu, Segment} from 'semantic-ui-react'
import {getLocaleOption, getSupportedLocaleOptions, SupportedLocale} from '../core/locales';
import {SessionContext} from '../types';
import {SettingsContext} from "../pages/_app";
import {useRouter} from "next/router";
import {DropdownItemProps} from "semantic-ui-react/dist/commonjs/modules/Dropdown/DropdownItem";
import Head from "next/head";

interface ProfileMenuItemProps {
    sessionContext?: SessionContext;
}

const ProfileMenuItem: FC<ProfileMenuItemProps> = (props: ProfileMenuItemProps): ReactElement => {
    const {sessionContext} = props;

    const {formatMessage: t} = useIntl();

    if (sessionContext) {
        const {userContext} = sessionContext;
        const {username, firstName, lastName} = userContext;
        return (
            <Dropdown simple item text={t({id: 'main-menu.menu-item.profile'})}>
                <Dropdown.Menu>
                    <Dropdown.Item disabled>{`${firstName} ${lastName} (${username})`}</Dropdown.Item>
                </Dropdown.Menu>
            </Dropdown>
        );
    } else {
        return (
            <Dropdown simple item disabled text={t({id: 'main-menu.menu-item.profile'})}/>
        );
    }
};

interface HeaderMenuProps {
    sessionContext?: SessionContext;
}

export const HeaderMenuFragment: FC<HeaderMenuProps> = (props: HeaderMenuProps): ReactElement => {
    const {sessionContext} = props;

    const router = useRouter();
    const {formatMessage: t} = useIntl();
    const {locale, setLocale} = useContext(SettingsContext);
    const currentLocale = getLocaleOption(locale);
    const supportedLocales = getSupportedLocaleOptions();
    const filteredLocales = supportedLocales.filter(l => l.key !== currentLocale.key);

    const onLocaleChange = (event: React.MouseEvent<HTMLDivElement>, data: DropdownItemProps) => {
        const {value: newLocale} = data;
        if (!!newLocale) {
            setLocale(newLocale as SupportedLocale);
        }
    }

    return (
        <>
            <Head>
                <title>{t({id: 'site.title'})}</title>
                <meta property="og:title" content="AcnTech Order Portal" key="title"/>
            </Head>
            <Segment basic className="main-menu">
                <Menu>
                    <Menu.Menu position='left'>
                        <Link href="/">
                            <Menu.Item>
                                <Image src="/assets/favicon-32x32.png" className="main-menu-icon"/>
                                <FormattedMessage id="main-menu.title"/>
                            </Menu.Item>
                        </Link>
                    </Menu.Menu>
                    <Menu.Menu position='right'>
                        <Dropdown labeled item icon='globe' className="main-menu-locale" text={currentLocale.text}>
                            <Dropdown.Menu>
                                {
                                    filteredLocales.map(locale => {
                                        return (
                                            <Dropdown.Item key={locale.key} value={locale.value} text={locale.text}
                                                           onClick={onLocaleChange}/>
                                        );
                                    })
                                }
                            </Dropdown.Menu>
                        </Dropdown>
                        <ProfileMenuItem sessionContext={sessionContext}/>
                    </Menu.Menu>
                </Menu>
            </Segment>
        </>
    );
};
