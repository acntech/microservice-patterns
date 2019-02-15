# Event Notification Pattern
This module explores the use of asynchronous event notification. Each service holds its own state in a local data store. When the state of a service is modified it will publish an notification event. The events are "thin" messages that are distributed between the microservices using a message broker in a publish/subscribe fashion.
