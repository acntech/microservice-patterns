import {Head, Html, Main, NextScript} from "next/document";
import {FC, ReactElement} from "react";

const Document: FC = (): ReactElement => {
    return (
        <Html lang="en">
            <Head>
                <link rel="icon" type="image/png" sizes="16x16" href="/assets/favicon-16x16.png"/>
                <link rel="icon" type="image/png" sizes="32x32" href="/assets/favicon-32x32.png"/>
                <link rel="icon" type="image/png" sizes="192x192" href="/assets/favicon-192x192.png"/>
                <link rel="apple-touch-icon" href="/assets/apple-touch-icon.png"/>
            </Head>
            <body>
            <Main/>
            <NextScript/>
            </body>
        </Html>
    )
};

export default Document;
