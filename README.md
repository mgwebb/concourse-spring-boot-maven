# Concourse for Spring Boot & Maven

This is an example Concourse pipeline for a Spring Boot web app built with Maven.

Prerequisites:
- [Java 8 SDK]

## Create a Sprint Boot web app

Using the [Spring Boot CLI]:

```
spring init concourse-spring-boot-maven --dependencies=web -build=maven
```

or you can use [Spring Initializr](https://start.spring.io/).

## Check the web app runs locally

```
./mvnw clean spring-boot:run
open http://localhost:8080
```


[Java 8 SDK]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Spring Boot CLI]: http://pivotal-guides.cfapps.io/frameworks/spring/getting-started/
[Spring Initializr]: https://start.spring.io/
