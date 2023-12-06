# Microservice Patterns

Projects for exploring different microservice architectures and patterns. Each subfolder focuses on a
specific area of patterns.

## Business case

The same business case is used in all the examples in order to make it easier to compare the different
patterns. This business case is that of an ordering system where customers can order products and have
them shipped. The architecture of the system is typically comprised of the following microservices:

* **Ordering**
  * Service where customers can create orders and add order items.
  * http://localhost:9002
* **Warehouse**
  * Service that maintains products and product inventory.
  * http://localhost:9003
* **Shipping** (optional)
  * Service that ships an order when it has been completed.
  * http://localhost:9004
* **Billing** (optional)
  * Service that sends invoices to customers when orders have been completed and shipped.
  * http://localhost:9005

As well there is an order portal web frontend.

[/ frontend / portal / README](https://github.com/acntech/microservice-patterns/tree/develop/frontend/portal)

The portal uses an API Gateway to proxy all communication with the microservices, and to create a common
facade for API calls.

[/ frontend / api-gateway / README](https://github.com/acntech/microservice-patterns/tree/develop/frontend/api-gateway)

## Pattern areas

Below are the pattern areas that are being explored. Each subfolder has its own README with further details.

* **Communication Patterns**
  * Examples that explore different patterns related to communication between microservices.
  * [/ communication-patterns / README](https://github.com/acntech/microservice-patterns/tree/develop/communication-patterns)

* **Reliability Patterns**
  * Examples that explore different patterns related to microservice reliability and resilience.
  * [/ reliability-patterns / README](https://github.com/acntech/microservice-patterns/tree/develop/reliability-patterns)

* **Observability Patterns**
  * Examples that explore different patterns related to microservice observability and monitoring.
  * [/ observability-patterns / README](https://github.com/acntech/microservice-patterns/tree/develop/observability-patterns)

* **Security Patterns**
  * Examples that explore different patterns related to microservice security and privacy.
  * [/ security-patterns / README](https://github.com/acntech/microservice-patterns/tree/develop/security-patterns)

## Talking Points

* Microservices
    * Circuit Breaker
    * Monitoring
* Event Driven
    * General
        * Delivery guaranty
        * Recover from errors
        * Orchestrated vs Coordinated
    * Event Notifications
        * Decoupling
        * Thin events
        * Fuzzy side effects
        * Process flow is not obvious
        * Events vs Commands
        * Callback storm
        * Backpressure
    * Event-Carried State Transfer
        * More decoupling
        * Local copy of state
        * Higher availability
        * Eventual consistency
    * Event Sourcing
        * Event store is principal source of truth
        * Recreate application state from events
        * Aggregates/Snapshots and history
        * Schema and versioning
        * Application state can be in-memory
        * Service mesh/Memory grid between instances
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
            * Resource Owner Password Credentials: used with trusted Applications, such as those owned by the service
              itself
            * Client Credentials: used with Applications API access
