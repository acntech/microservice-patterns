# Event State Transfer Pattern
This example explores the use of asynchronous event driven state transfer. Each service holds its own
state in a local data store. When the state of a service is modified a complete record of that change
is published as an event. The events are distributed between the microservices using a message broker
in a publish/subscribe fashion.
