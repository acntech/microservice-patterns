/** @type {import('next').NextConfig} */
module.exports = {
    reactStrictMode: true,
    swcMinify: true,
    rewrites: () => {
        return [
            {
                source: "/api/orders",
                destination: "http://localhost:9000/api/orders"
            },
            {
                source: "/api/orders/:orderId",
                destination: "http://localhost:9000/api/orders/:orderId"
            },
            {
                source: "/api/orders/:orderId/items",
                destination: "http://localhost:9000/api/orders/:orderId/items"
            },
            {
                source: "/api/orders/:orderId/items/:itemId",
                destination: "http://localhost:9000/api/orders/:orderId/items/:itemId"
            },
            {
                source: "/api/items/:itemId",
                destination: "http://localhost:9000/api/items/:itemId"
            },
            {
                source: "/api/products",
                destination: "http://localhost:9000/api/products"
            },
            {
                source: "/api/products/:productsId",
                destination: "http://localhost:9000/api/products/:productsId"
            }
        ]
    },
    i18n: {
        locales: ["en", "no"],
        defaultLocale: "en"
    }
}
