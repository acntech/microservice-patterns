# WebServices REST

This example explores the use of synchronous REST webservices.

## Architecture

The architecture is based on microservices that communicate using synchronous REST over HTTP.

### Microservices

The system is made up of the following microservices:

* Warehouse
* Ordering
* Billing
* Shipping

### Data Stores

Each microservice has its own data store, and is responsible to maintain its state in the data store.

### User Interface

The user interface uses the exposed REST APIs of the microservices to drive its view logic.

## Operations

### Spring Boot Microservice Apps

The Spring Boot based microservices uses an embedded Apache Tomcat server.

#### Runtime

The applications can be started from the source code using the `spring-boot-maven-plugin` by running
the command `mvn spring-boot:run`.
They can also be started from the archive using the command `java -jar <arhive>.jar`.

#### Spring profiles:

Supply Spring profiles to specify configuration options either using en environment variable
`SPRING_PROFILES_ACTIVE=<profile>` or a JVM option `-Dspring.profiles.active=<profile>`.

* `development`: Development environment using an embedded H2 in-memory database (used if no profiles are specified).
* `production`: Production environment using a Postgres database.

### ReactJS UI App

The React/Redux based front end application can be started from source code using Yarn `yarn dev-start`
or NPM `npm run start`.
