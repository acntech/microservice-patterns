# Reliability Patterns - Circuit Breaker

Consists of two services:

- ordering-service (utilising circuit-breaker)
- warehouse-service

The services exposes REST endpoints.
An order is placed in the ordering service. Ordering calls warehouse to reserve the order.
Ordering uses circuit-breaker to protect itself and the warehouse service downstream.

The example uses Resilience4J as the circuit-breaker framework.

The warehouse service endpoint fails randomly in order to show the circuit-breaker functionality.

##### Run example

Build and start each application (spring-boot).

Call the ordering services by doing a `POST` to `localhost:9010/api/orders` with the following request body:

```json
{
  "customerId": "60fa2609-31d4-4876-9677-c28a1dd93f17",
  "name": "My Order"
}
```

Then, with the order-id that is returned in the location header, make another `POST`
to `localhost:9010/api/orders/{order-id}/items` with the following request body:

```json
{
  "productId": "24b6d840-80b7-44e6-9216-d8ee99afa60a",
  "quantity": 3
}
```

##### Hystrix dashboard

Go to `http://localhost:9010/api/hystrix` (ordering service) and paste
`http://localhost:9010/api/actuator/hystrix.stream` in the input-field.
