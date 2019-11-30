# Circuit Breaker Pattern

Consists of two services:
- ordering-service (utilising circuit-breaker)
- warehouse-service

The services exposes REST endpoints.
An order is placed in order the order service. Order calls warehouse to reserve the order.
Order uses circuit-breaker to protect itself and the warehouse service downstream.

The example uses netflix-hystrix as circuit-breaker framework. The order-services
also exposes the hystrix dashboard that provides monitoring of the circuits.

The warehouse-service endpoint fails randomly in order to show the
circuit-breaker functionality.

##### Run example
Build and start each application (spring-boot). 

Call the order services by doing a `POST` to `localhost:9010/api/orders` with the following request body:
```json
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
Go to `http://localhost:9010/api/hystrix` (order application) and paste 
`http://localhost:9010/api/actuator/hystrix.stream` in the input-field.

The warehouse circuit-breaker configuration can be found in the 
`no.acntech.reservation.consumer.ReservationRestConsumer` class.
