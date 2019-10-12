# Charging station app

Based on [Spring Boot](http://projects.spring.io/spring-boot/)

## Test coverage
100% of classes and 91% lines of code are covered with tests according to Intellij Idea report

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 5.6.2](https://gradle.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.chargestation.ChargeStationApplication` class from your IDE.

Alternatively you can use the Gradle wrapper from command line like so:

```shell
./gradlew bootRun
```

Also you can run provided jar file with command line:

```shell
java -jar charge-station-0.0.1-SNAPSHOT.jar
```
