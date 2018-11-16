# Synchronous HTTP between services

Consists of three services:
- order
- warehouse
- shipping

The services exposes REST endpoints.
An order is placed in order the order service. Order calls warehouse services to make a reservation
and then calls shipping service to ship the order.

This example demonstrates the consistency challenges between services with local datastores when doing synchronous HTTP calls.

If, for example, shipping fails after the warehouse has been updated, you won’t find any related shipping data and the order data will be rolled back as well.

These types of inconsistencies can be solved through best-effort technical implementation (compensating transactions, logging, etc) and/or manual intervention (update the warehouse manually based on log data, etc).
Examples of this will follow.

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
