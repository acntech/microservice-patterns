# Synchronous HTTP between services

Consists of two services:
- order (utilising circuit-breaker)
- shipping

The services exposes REST endpoints.
An order is placed in order the order service. Order calls shipping to ship the order.
Order uses circuit-breaker to protect itself and the shipping service downstream.

The example uses netflix-hystrix as circuit-breaker framework. The order-services
also exposes the hystrix dashboard that provides monitoring of the circuits.

The shipping-service endpoint fails randomly in order to show the
circuit-breaker functionality.

##### Run example
Build and start each application (spring-boot). 

Call the order services by doinga `POST` to `localhost:8080/orders` with the following requestbody:
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

##### Hystrix dashboard
Go to `http://localhost:8080/hystrix` (order application) and paste 
`http://localhost:8080/actuator/hystrix.stream` in the input-field.

The shipping circuit-breaker configuration can be found in the 
`no.acntech.order.integration.shipping.ShippingRestClient` class.
