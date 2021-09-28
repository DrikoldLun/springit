#Springit

This is a reddit clone project using spring boot 2.5.5

## Springboot essentials

### Springboot devtools
automated restart set
preference -> build... -> compiler -> auto build
preference -> advanced -> auto-make

### Configuration and Properties
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

### Profile
1. Let some operations happen only in certain profile
Profile(String) in application.java
set spring.profiles.active = [any String] in application.properties

2.configure different environment using profiles
touch a new applicaton-[profile].properties under resourses/ and set set spring.profiles.active = [any String] in application.properties

### Debugging and Logging
#### debugging
can use evaluate to check the API
ApplicationContext

#### logging
1. set logging level: set logging.level.root=TRACE in application.properties
2. logging for a specific class
import org.slf4j.Logger
import org.slf4j.LoggerFactory
Logger log = LoggerFactory.getLoggers(SpringitApplication.class)