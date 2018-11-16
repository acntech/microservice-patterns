# Asynchronous messaging with JMS between services

Consists of three services that communicates asynchronously:
- order
- warehouse
- shipping

An order is placed in the order service. Order sends a reservation message to the warehouse service.
Warehouse sends a confirmation message with the status of the reservation. 
If the reservation was successfull, the order service
completes the order and sends a message to the shipping service.

Messaging model is defined in the messaging-message-model module.

##### Run example
Start active mq broker by running `docker-compose up` in current directory.
Go to `http://localhost:8161/admin/` to inspect the ActiveMQ broker (u/p: admin/admin)

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
