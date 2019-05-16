# Synchronous HTTP between services

Consists of two services:
- order (utilising retries)
- shipping

The services exposes REST endpoints.
An order is placed in order the order service. Order calls shipping to ship the order.
Order uses retries if shipping service fails.

The shipping-service endpoint fails randomly on every call in order to show retry functionality

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

The shipping retry configuration can be found in the 
`no.acntech.order.integration.shipping.ShippingRestClient` class.
