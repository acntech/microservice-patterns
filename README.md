# Microservice Patterns
Projects for exploring different microservice architectures and patterns

[![Build Status](https://travis-ci.org/acntech/microservice-patterns.svg?branch=develop)](https://travis-ci.org/acntech/microservice-patterns)

* Microservices
  * Cirquit Breaker
  * Monitoring
  * Security
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