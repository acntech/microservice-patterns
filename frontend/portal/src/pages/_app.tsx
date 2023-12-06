import type {AppProps} from "next/app";
import React, {FC, ReactElement} from "react";
import {CookiesProvider} from "react-cookie";
import {Provider as StateStoreProvider} from 'react-redux';
import {HeaderMenu} from "../components";
import {SessionProvider, SettingProvider} from "../providers";
import store from "../state/store";

import {config} from "@fortawesome/fontawesome-svg-core";
import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-svg-core/styles.css";
import "../styles/globals.css";

config.autoAddCss = false;

const App: FC<AppProps> = ({Component, pageProps}: AppProps): ReactElement => {

    return (
        <CookiesProvider>
            <SettingProvider>
                <StateStoreProvider store={store}>
                    <SessionProvider>
                        <HeaderMenu/>
                        <Component {...pageProps} />
                    </SessionProvider>
                </StateStoreProvider>
            </SettingProvider>
        </CookiesProvider>
    );
}

export default App;
