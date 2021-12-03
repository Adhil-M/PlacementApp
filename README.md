# PlacementApp


## Requirements

For building and running the application you need:

- JDK 1.8
- Gradle 7.3

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.dbms.placementapp.PlacementappApplication` class from your IDE.



```shell
gradle clean
gradle build
java -jar  build/libs/placementapp-0.0.1-SNAPSHOT-plain.jar
```

## Modules

### common
contains common constants and utilities.
### configurations
contains database configurations
### controllers
contains controllers for admin,student and user
### models
contains Model classes for different entities
### repositories 
contains sping jpa repositories for data access from postgresql
### services
contains services for admin,student and user 
