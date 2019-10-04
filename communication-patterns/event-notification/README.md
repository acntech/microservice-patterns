# Event Notification Pattern
This module explores the use of asynchronous event notification. Each service holds its own state in a local data store. When the state of a service is modified it will publish an notification event. The events are "thin" index that are distributed between the microservices using a message broker in a publish/subscribe fashion.

## Architecture
The architecture is based on a combination of synchronous reads and asynchronous writes, with messaging for notification of state change, in a CQRS style communication pattern.

### Microservices
The system is made up of the following microservices:

* Customers
* Warehouse
* Ordering
* Billing
* Shipping

### Data Stores
Each microservice has its own data store, and is responsible to maintain its state in the data store.

### Messaging
A message broker (event bus) for distributing notifications of state change between the microservices.

### User Interface
The user interface uses the exposed REST APIs of the microservices to drive its view logic.

## Operations

### Spring Boot Microservice Apps
The Spring Boot based microservices uses an embedded Apache Tomcat server.

The applications can be started from the source code using the `spring-boot-maven-plugin` by running the command `mvn spring-boot:run`.
They can also be started from the archive using the command `java -jar <arhive>.war`.

Supply Spring profiles to specify configuration options either using en environment variable `SPRING_PROFILES_ACTIVE=<profile>` or a JVM option `-Dspring.profiles.active=<profile>`.

Available profiles:
* `default` (used if no profiles are specified): Local environment using an embedded H2 in-memory database.
* `development`: Local environment using a Postgres database.

### ReactJS UI App
The React/Redux based front end application can be started from source code using Yarn `yarn dev-start` or NPM `npm run start`.

