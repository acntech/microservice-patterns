/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: false,
    swcMinify: true,
    rewrites: () => {
        return [
            {
                source: "/login",
                destination: "http://localhost:9000/login"
            },
            {
                source: "/login/oauth2/code/:registrationId",
                destination: "http://localhost:9000/login/oauth2/code/:registrationId"
            },
            {
                source: "/oauth2/authorization/:registrationId",
                destination: "http://localhost:9000/oauth2/authorization/:registrationId"
            },
            {
                source: "/_api/:resource*", // Static resources
                destination: "http://localhost:9000/_api/:resource*"
            },
            {
                source: "/api/:resource*", // API resources
                destination: "http://localhost:9000/api/:resource*"
            }
        ]
    },
    i18n: {
        locales: ["en", "no"],
        defaultLocale: "en"
    }
}
