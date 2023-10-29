import React, {FC, ReactElement, useContext, useEffect, useState} from 'react'
import {useIntl} from 'react-intl'
import {Image, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {faGlobe, faUser} from "@fortawesome/free-solid-svg-icons";
import {SupportedLocale, supportedLocales} from '../core/locales';
import {SessionContext} from '../types';
import {SettingsContext} from "../pages/_app";
import Head from "next/head";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Link from "next/link";

const LocaleMenuItem: FC = (): ReactElement => {
    const {locale, setLocale} = useContext(SettingsContext);

    const onLocaleChange = (eventKey: string | null) => {
        if (!!eventKey) {
            setLocale(eventKey as SupportedLocale);
        }
    }

    return (
        <NavDropdown className="me-3" align="end" onSelect={onLocaleChange}
                     title={<FontAwesomeIcon icon={faGlobe}/>}>
            {
                supportedLocales.map(supportedLocale => {
                    return (
                        <NavDropdown.Item key={supportedLocale}
                                          eventKey={supportedLocale}
                                          disabled={supportedLocale === locale}>
                            {supportedLocale.toUpperCase()}
                        </NavDropdown.Item>
                    );
                })
            }
        </NavDropdown>
    );
};

interface ProfileMenuItemProps {
    sessionContext?: SessionContext;
}

const ProfileMenuItem: FC<ProfileMenuItemProps> = (props: ProfileMenuItemProps): ReactElement => {
    const {sessionContext} = props;

    const [user, setUser] = useState<string>("N/A");

    useEffect(() => {
        if (!!sessionContext) {
            const {username, firstName, lastName} = sessionContext.userContext;
            setUser(`${firstName} ${lastName} (${username})`);
        }
    }, [sessionContext]);

    return (
        <NavDropdown className="me-3" align="end" title={<FontAwesomeIcon icon={faUser}/>}>
            <NavDropdown.Item disabled>{user}</NavDropdown.Item>
        </NavDropdown>
    );
};

interface HeaderMenuProps {
    sessionContext?: SessionContext;
}

export const HeaderMenuFragment: FC<HeaderMenuProps> = (props: HeaderMenuProps): ReactElement => {
    const {sessionContext} = props;

    const {formatMessage: t} = useIntl();

    return (
        <>
            <Head>
                <title>{t({id: 'site.title'})}</title>
                <meta property="og:title" content="AcnTech Order Portal" key="title"/>
            </Head>
            <header className="mb-5">
                <Navbar expand="lg" bg="dark" data-bs-theme="dark" className="p-4">
                    <Navbar.Brand>
                        <Link href="/">
                            <Image className="main-menu-icon" src="/assets/logo.png" alt="Logo"/>
                        </Link>
                    </Navbar.Brand>
                    <Nav className="ms-auto">
                        <LocaleMenuItem/>
                        <ProfileMenuItem sessionContext={sessionContext}/>
                    </Nav>
                </Navbar>
            </header>
        </>
    );
};
