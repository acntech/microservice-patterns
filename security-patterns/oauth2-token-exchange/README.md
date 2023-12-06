# Security Patterns - OAuth2 Token Exchange

This example explores protecting microservices using the OAuth2 Token Exchange security pattern.
* [RFC 6749](https://www.rfc-editor.org/rfc/rfc6749) - OAuth 2.0 Authorization Framework
* [RFC 6750](https://www.rfc-editor.org/rfc/rfc6750) - OAuth 2.0 Bearer Token Usage
* [RFC 7523](https://www.rfc-editor.org/rfc/rfc7523) - JWT Profile for OAuth 2.0 Client Grants
* [RFC 8693](https://www.rfc-editor.org/rfc/rfc8693) - OAuth 2.0 Token Exchange

The OIDC/OAuth2 flow is initiated by the frontend. The API Gateway is secured as an OAuth2 Client
using the Authorization Code grant. When the frontend fetches data from the microservices the
API Gateway exchanges the current OAuth2 Token with a more specialized OAuth2 Token, with increased
or decreased authorization rights. This token is then used as a Bearer Token in calls to the microservices
which are secured as OAuth2 resource servers.

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

### KeyCloak Setup

As of KeyCloak v22.0 the Token Exchange is a preview feature and needs to be explicitly enabled to use.

Use start argument `--features=preview` to enable all preview features. Alternatively use only
`--features=token_exchange`. In order to use Token Exchange between KeyCloak and other IdPs it is
necessary to also use `--features=admin_fine_grained_authz`.

See docs:
* [KeyCloak - Using Token Exchange](https://www.keycloak.org/docs/latest/securing_apps/#_token-exchange)

See articles:
* [Token Exchange using KeyCloak](https://medium.com/geekculture/token-exchange-using-keycloak-204da039b5e6)

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
