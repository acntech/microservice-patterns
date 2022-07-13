# Synchronous HTTP between services

Consists of two services:

- order (utilising timeout)
- shipping

The services exposes REST endpoints.
An order is placed in order the order service. Order calls shipping to ship the order.
Order uses timeout to protect itself against high latency in shipping service.

The shipping-service endpoint fails randomly in order to show the
timeout functionality.

##### Run example

Build and start each application (spring-boot).

Call the order services by doing a `POST` to `localhost:8080/orders` with the following requestbody:

```js
{
    "orderlines": [
        {
            "productId": 42,
            "quantity": 5
        }
    ]
}
``` 

The shipping timeout configuration can be found in the
`no.acntech.order.integration.shipping.ShippingRestClient` class.
