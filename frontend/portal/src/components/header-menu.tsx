import React, {FC, ReactElement, useContext} from 'react'
import {useIntl} from 'react-intl'
import Head from "next/head";
import Link from "next/link";
import {Image, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCartShopping, faGlobe, faUser} from "@fortawesome/free-solid-svg-icons";
import {Session, Settings} from "../providers";
import {SupportedLocale, supportedLocales} from '../core/locales';

const CartMenuItem: FC = (): ReactElement => {

    return (
        <Nav.Link className="me-3" href="/cart">
            <FontAwesomeIcon icon={faCartShopping}/>
        </Nav.Link>
    );
};

const LocaleMenuItem: FC = (): ReactElement => {
    const {locale, setLocale} = useContext(Settings);

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
}

const ProfileMenuItem: FC<ProfileMenuItemProps> = (props): ReactElement => {
    const {userContext} = useContext(Session);
    const {username, firstName, lastName} = userContext;

    return (
        <NavDropdown className="me-3" align="end" title={<FontAwesomeIcon icon={faUser}/>}>
            <NavDropdown.Item disabled>{`${firstName} ${lastName} (${username})`}</NavDropdown.Item>
        </NavDropdown>
    );
};

interface MainMenuProps {
    hideMenu?: boolean;
}

const MainMenu: FC<MainMenuProps> = (props): ReactElement => {
    const {hideMenu} = props;

    if (!!hideMenu) {
        return <></>;
    } else {
        return (
            <Nav className="ms-auto">
                <CartMenuItem/>
                <LocaleMenuItem/>
                <ProfileMenuItem/>
            </Nav>
        );
    }
};

export interface HeaderMenuProps {
    hideMenu?: boolean;
}

export const HeaderMenu: FC<HeaderMenuProps> = (props): ReactElement => {
    const {hideMenu} = props;

    const {formatMessage: t} = useIntl();

    return (
        <>
            <Head>
                <title>{t({id: 'site.title'})}</title>
                <meta property="og:title" content="AcnTech Order Portal" key="title"/>
            </Head>
            <header className="mb-3">
                <Navbar expand="lg" bg="dark" data-bs-theme="dark" className="p-4">
                    <Navbar.Brand>
                        <Link href="/">
                            <Image className="main-menu-icon" src="/assets/logo.png" alt="Logo"/>
                        </Link>
                    </Navbar.Brand>
                    <MainMenu hideMenu={hideMenu}/>
                </Navbar>
            </header>
        </>
    );
};
