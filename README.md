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

## Create a Concourse pipeline

The first job in the pipeline will package the app using Maven.

```
mkdir ci/
touch ci/pipeline.yml
```

The package job will get the source code and run a maven command:

```yaml
# ci/jobs.yml
jobs:
- name: package
  plan:
  - get: source-code
    trigger: true
  - task: package
    privileged: true
    file: source-code/ci/tasks/package.yml
```

This job requires `source-code`, which is a git resource:

```yaml
# ci/resources.yml
resources:
- name: source-code
  type: git
  source:
    uri: REPLACE_WITH_YOUR_GIT_REPOSITORY_URL
    branch: master
```

And it will run the package task, which tells Concourse to:

- create a linux container
- using the latest Java Dockerfile published on [Docker Hub]
- add the `source-code` resource to the container
- run a command in the container

```yaml
# ci/tasks/package.yml
platform: linux

image_resource:
  type: docker-image
  source:
    repository: java
    tag: latest

inputs:
- name: source-code

run:
  path: "source-code/mvnw"
  args: ["--file", "source-code/pom.xml", "package"]
```

> Note: there is no need to run `./mvnw clean` because Concourse creates a new container for every task.


[Java 8 SDK]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Spring Boot CLI]: http://pivotal-guides.cfapps.io/frameworks/spring/getting-started/
[Spring Initializr]: https://start.spring.io/
[Docker Hub]: https://hub.docker.com/_/java/
