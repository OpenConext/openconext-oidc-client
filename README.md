# openconext-oidc-client

OpenID Connect library for Java apps acting as Relying Party. This repo contains two projects, the actual library for
securing a Spring Boot application with OpenID Connect and an example server application for reference purposes.

## [Getting started](#getting-started)

### [System Requirements](#system-requirements)

- Java 21
- Maven 3

First install Java 21 with a package manager
and then export the correct the `JAVA_HOME`. For example on macOS:

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/openjdk-21.jdk/Contents/Home/
```

### [Building and running](#building-and-running)

This project uses Spring Boot and Maven. To run locally, type:

```bash
cd ./client-lib
mvn clean install
cd ../example-server
mvn spring-boot:run
```

If you visit http://localhost:8881/api/v1/users/me you will be redirected to login.