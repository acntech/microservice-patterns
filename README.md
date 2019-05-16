# Microservice Patterns
Projects for exploring different microservice architectures and patterns. Each module/example focuses on a specific pattern for Inter-Process Communication (IPC) between microservices.

[![Build Status](https://travis-ci.org/acntech/microservice-patterns.svg?branch=develop)](https://travis-ci.org/acntech/microservice-patterns)

The same use case is used in all the examples in order to make it easier to compare the different patterns. This use case is that of an ordering system where customers can order products and have them shipped. The architecture of the system is typically comprised of the following microservices:

* Ordering
  * Service where customers can create orders and add order items.
* Warehouse
  * Service that maintains products and product inventory.
* Shipping
  * Service that ships an order when it has been completed.
* Billing
  * Service that sends invoices to customers when orders have been completed and shipped.
* Customers
  * Service that maintains customer records.

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

## Talking Points

* Microservices
  * Circuit Breaker
  * Monitoring
* Event Driven
  * General
    * Delivery guaranty
    * Recover from errors
  * Event Notifications
    * Decoupling
    * Thin events
    * Fuzzy side effects
    * Process flow is not obvious
    * Events vs Commands
    * Callback pressure
  * Event-Carried State Transfer
    * More decoupling
    * Local copy of state
    * Higher availability
    * Eventual consistency
  * Event Sourcing
    * Event store is principal source of truth
    * Event log can recreate application state
    * Snapshots and history
    * Schema and versioning
    * Application state can be in-memory
    * Memory grid between instances
    * Store all events
    * Ability to look back in time
    * No intermediates
  * CQRS
    * Consider access pattern
    * Increased complexity
* Security
  * OpenID Connect (OIDC): Authentication layer which is used on top of OAuth2
  * OAuth2: Authorization protocol
    * Roles
      * Resource Owner: End user
      * Client: Application needing access to Resource Server on behalf of Resource Owner
      * Resource Server: API that Client want to access
      * Authorization Server: API that grants access
    * Grant Types
      * Authorization Code: Used with server-side Applications
      * Implicit: used with Mobile Apps or Web Applications (applications that run on the user's device)
      * Resource Owner Password Credentials: used with trusted Applications, such as those owned by the service itself
      * Client Credentials: used with Applications API access
