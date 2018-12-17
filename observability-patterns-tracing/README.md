# Distributed tracing

Example consists of two services:
- order
- shipping

The services exposes REST endpoints. An order is placed in order the order service. Order calls shipping to ship the order.

The applications demonstrates distributed tracing by utilising 
[spring-cloud-sleuth](https://spring.io/projects/spring-cloud-sleuth) and [zipkin](https://github.com/openzipkin/zipkin)

##### Run example
Build and start each application (spring-boot).

Start zipkin by running `docker-compose up` in the current directory.

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

##### Tracing
Each call to the order-service creates a new trace-id which is propagated to remote services. This makes it possible to trace a call between services.

The trace-id is sent through http-headers and this is done transparently by `spring-cloud-sleuth`.

Each service can also create internal spans. A span is basically a unit of work.

Example log from a call to order:
```
2018-12-17 11:22:10.119 DEBUG [order-service,4a9dcdd5d89e94fb,53bd2c98e1610ea8,true] 16844 --- [nio-8080-exec-2] no.acntech.order.resource.OrderResource  : OrderResource#submit for order: Order(id=null, orderstatus=null, orderlines=[Orderline(id=null, productId=42, quantity=2)], shippingId=null)
2018-12-17 11:22:10.134 DEBUG [order-service,4a9dcdd5d89e94fb,1c1cb4be684c9803,true] 16844 --- [nio-8080-exec-2] no.acntech.order.service.OrderService    : Calling OrderRepository#save for order: Order(id=null, orderstatus=null, orderlines=[Orderline(id=null, productId=42, quantity=2)], shippingId=null)
2018-12-17 11:22:10.174 DEBUG [order-service,4a9dcdd5d89e94fb,1c1cb4be684c9803,true] 16844 --- [nio-8080-exec-2] no.acntech.order.service.OrderService    : Calling ShippingRestClient#ship for order: Order(id=1, orderstatus=null, orderlines=[Orderline(id=2, productId=42, quantity=2)], shippingId=null)
2018-12-17 11:22:10.175 DEBUG [order-service,4a9dcdd5d89e94fb,947516a78a9738a9,true] 16844 --- [nio-8080-exec-2] n.a.o.i.shipping.ShippingRestClient      : Posting to /shipments endpoint for orderId=1
2018-12-17 11:22:10.251 DEBUG [order-service,4a9dcdd5d89e94fb,947516a78a9738a9,true] 16844 --- [nio-8080-exec-2] n.a.o.i.shipping.ShippingRestClient      : Received response for orderId=eef5b0a4-7df4-4d31-a962-57fd32c213d8, shipmentId={}1
2018-12-17 11:22:10.251  INFO [order-service,4a9dcdd5d89e94fb,1c1cb4be684c9803,true] 16844 --- [nio-8080-exec-2] no.acntech.order.service.OrderService    : Order with orderId=1 shipped! shippingId=eef5b0a4-7df4-4d31-a962-57fd32c213d8
2018-12-17 11:22:10.251 DEBUG [order-service,4a9dcdd5d89e94fb,1c1cb4be684c9803,true] 16844 --- [nio-8080-exec-2] no.acntech.order.service.OrderService    : Updating Order#orderStatus to COMPLETED
2018-12-17 11:22:10.251 DEBUG [order-service,4a9dcdd5d89e94fb,1c1cb4be684c9803,true] 16844 --- [nio-8080-exec-2] no.acntech.order.service.OrderService    : Returning from OrderService#submit
2018-12-17 11:22:10.281 DEBUG [order-service,4a9dcdd5d89e94fb,53bd2c98e1610ea8,true] 16844 --- [nio-8080-exec-2] no.acntech.order.resource.OrderResource  : Order successfully created, responding with HttpStatus=200 OK
```

The default logging-pattern in sleuth is: `[application-name,trace-id,span-id,span-exported]`.
Notice that `4a9dcdd5d89e94fb` is the global trace-id and each method in the service creates their own local spans.

The trace-id is propagated to the remote shipping service:
```
2018-12-17 11:22:10.235  INFO [shipping-service,4a9dcdd5d89e94fb,29f7f6423a1b942c,true] 16730 --- [nio-8082-exec-9] n.a.shipping.resource.ShippingResource   : Order with orderId=1 shipped with shippingId=eef5b0a4-7df4-4d31-a962-57fd32c213d8
```

The sample uses console-logging for simplicity, but this pattern should preferably be combined with aggregated logging.

##### Zipkin
Go to `http://localhost:9411/` to access zipkin. The goal of zipkin is to be able to:
- Search for trace-id's
- Inspect calls throughout the system
- See detailed performance metrics for calls
- See dependencies between applications
- Etc..

<img src="https://i.imgur.com/RUW2BNE.png" width="50%" height="50%">

<img src="https://i.imgur.com/qCmhI8a.png" width="50%" height="50%">