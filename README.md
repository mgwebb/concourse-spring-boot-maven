# Concourse for Spring Boot & Maven

This is an example Concourse pipeline for a Spring Boot web app built with Maven.

Prerequisites (and versions used):

- Mac OS X (10.10.5)
- [Homebrew] (0.9.5)
- [Java 8 SDK] (build 1.8.0_73-b02)
- [Vagrant] (v1.8.1)
- [VirtualBox] (v5.0.16)

```sh
brew cask install java
brew cask install vagrant
brew cask install virtualbox
```

## Create a Sprint Boot web app

You can use [Spring Initializr] and download a Spring Boot app with the `web` dependency, or install the Spring Boot CLI:

```sh
brew update
brew tap pivotal/tap
brew install springboot
```

And run:

```
spring init concourse-spring-boot-maven --dependencies=web -build=maven
```

## Check the web app runs locally

```sh
./mvnw clean spring-boot:run
open http://localhost:8080
```

## Create a Concourse pipeline

The first job in the pipeline will package the app using Maven.

```sh
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
- using the [Java 8 Dockerfile] published on Docker Hub
- add the `source-code` resource to the container
- run a command in the container

```yaml
# ci/tasks/package.yml
platform: linux

image_resource:
  type: docker-image
  source:
    repository: java
    tag: "8"

inputs:
- name: source-code

run:
  path: "source-code/mvnw"
  args: ["--file", "source-code/pom.xml", "package"]

params:
  MAVEN_BASEDIR: source-code
```

> Note: there is no need to run `./mvnw clean` because Concourse creates a new container for every task.

## Start Concourse lite

The Concourse team provides a lite version that can be run locally:

```sh
vagrant init concourse/lite
vagrant up
```

At the time of writing the version was 0.75.0.

## Download the Concourse CLI

```sh
open http://192.168.100.4:8080
```

Download the `fly` binary from the Concourse, by clicking on the Apple icon.
Then make it executable and put it in your $PATH, for example:

```sh
install ~/Downloads/fly /usr/local/bin/fly
```

## Create the Concourse pipeline

Create a new Concourse target for `fly` called `lite` and login:

```sh
fly --target lite login --concourse-url http://192.168.100.4:8080
```

Create a Concourse pipeline called `spring-boot-maven` using the resources and jobs.

```sh
fly --target lite set-pipeline --pipeline spring-boot-maven --config <(cat ci/resources.yml ci/jobs.yml)
```

Unpause the new pipeline using `fly`, or click Play in the web UI.

```sh
fly --target lite unpause-pipeline --pipeline spring-boot-maven
```


[Homebrew]: http://brew.sh/
[Java 8 SDK]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[Spring Initializr]: https://start.spring.io/
[Java 8 Dockerfile]: https://hub.docker.com/_/java/
[Vagrant]: https://www.vagrantup.com/downloads.html
[VirtualBox]: https://www.virtualbox.org/wiki/Downloads
