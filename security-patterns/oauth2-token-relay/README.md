# Security Patterns - OAuth2 Token Relay

This example explores protecting microservices using the OAuth2 Token Relay security pattern.
* [RFC 6749](https://www.rfc-editor.org/rfc/rfc6749) - OAuth 2.0 Authorization Framework
* [RFC 6750](https://www.rfc-editor.org/rfc/rfc6750) - OAuth 2.0 Bearer Token Usage

The OIDC/OAuth2 flow is initiated by the frontend. The API Gateway is secured as an OAuth2 Client
using the Authorization Code grant. When the frontend fetches data from the microservices the
API Gateway simply relays the OAuth2 Token as a Bearer Token as-is in calls to the microservices which
are secured as OAuth2 resource servers.

## Architecture

The architecture is based on microservices that communicate using synchronous REST over HTTP.

### Microservices

The system is made up of the following microservices:

* API Gateway
* Ordering Service
* Warehouse Service

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
