# Event Sourcing Pattern

This example explores the use of event sourcing. System state is maintained in a centralized event store. When state is
modified through a microservice a record of that change is published as an event. These events are stored in the event
store which acts as an event ledger or commit log. Microservices use snapshots or aggregates of the event ledger to
represent the current state of the system. These aggregates are continuously updated as new events are committed to the
event ledger.
