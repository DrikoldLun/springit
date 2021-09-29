#Springit

This is a reddit clone project using spring boot 2.5.5

## 1. Springboot essentials

### 1.1 Springboot devtools
automated restart set
preference -> build... -> compiler -> auto build
preference -> advanced -> auto-make

### 1.2 Configuration and Properties
configuration
resources/application.properties
change configuration like auto restart/port
java/*/config/SpringitProperties.java
eg. server.port=8085/ spring.devtools.restart.enabled=false

configuration processor
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
generate meta-data under target/classes/META_INF/*.jason

### 1.3 Profile
1. Let some operations happen only in certain profile
Profile(String) in application.java
set spring.profiles.active = [any String] in application.properties

2.configure different environment using profiles
touch a new applicaton-[profile].properties under resourses/ and set set spring.profiles.active = [any String] in application.properties

### 1.4 Debugging and Logging
#### debugging
can use evaluate to check the API
ApplicationContext

#### logging
1. set logging level: set logging.level.root=TRACE in application.properties for all classes
2. logging for a specific class
import org.slf4j.Logger
import org.slf4j.LoggerFactory
Logger log = LoggerFactory.getLoggers(SpringitApplication.class)
log.error(String)
set logging.level.com.lunz.springit=DEBUG

### 1.5 actuator
add some info of app
info.application.(name/description/version)
management.endpoint.health.show-details=always
management.endpoints.web.exposure.include=health,info (expose the endpoints)
all in application.properties

## 2. Spring MVC model
### 2.1 Spring data JPA
Spring data is a large project containing tons of modules like support for JDBC, JPA, MongoDB/Redis
JPA is just the specification and by itself isn’t all that usefull. What you need is a JPA provider that implements the specification like
Hibernate

### Entities
Must use annotations to let Spring know the class we created are entities, use @Entity
@ID annotation specifies the primary key of the entity
@GeneratedValue annotation provides strategies for the generation of primary key values

### Project Lombok Refactor
Refactor, right click package, select refactor and rename, it will automatically update the name of this package in all files
Needed for Lombok Project: turn on annotation processor - preference/compiler/annotation processors/enable annotation processing
@NoArgsConstructor to replace no args constructor
@Data to replace setter and getter, tostring and hashequal
@NonNull for a certain property: this is required, automatically create a constructor that instantiates this object