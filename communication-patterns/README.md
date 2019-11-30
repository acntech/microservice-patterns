# Communication Patterns
Projects for exploring different patterns related to communication. Each module/example focuses on a specific pattern for communication between microservices.

## Modules

##### REST
TODO

##### Messaging
TODO

##### Event Notification
This module explores the use of asynchronous event notification. Each service holds its own state in a local data store. When the state of a service is modified it will publish an notification event. The events are "thin" index that are distributed between the microservices using a message broker in a publish/subscribe fashion.

##### Event State Transfer
This module explores the use of asynchronous event state transfer. Each service holds its own state in a local data store. When the state of a service is modified a complete record of that change is published as an event. The events are distributed between the microservices using a message broker in a publish/subscribe fashion.

##### Event Sourcing
This module explores the use of event sourcing. System state is maintained in a centralized event store. When state is modified through a microservice a record of that change is published as an event. These events are stored in the event store which acts as an event ledger or commit log. Microservices use snapshots or aggregates of the event ledger to represent the current state of the system. These aggregates are continuously updated as new events are committed to the event ledger.

##### Event Streaming
