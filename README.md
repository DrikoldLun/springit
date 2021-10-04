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
lsof -i:8080 check which process is occupying 8080 port

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

### 2.2 Entities
Must use annotations to let Spring know the class we created are entities, use @Entity
@ID annotation specifies the primary key of the entity
@GeneratedValue annotation provides strategies for the generation of primary key values

### 2.3 Project Lombok Refactor
Refactor, right click package, select refactor and rename, it will automatically update the name of this package in all files
Needed for Lombok Project: turn on annotation processor - preference/compiler/annotation processors/enable annotation processing
@RequiredArgsConstructor
@Getter @Setter
@NoArgsConstructor
above to replace constructor, getter and setter
@NonNull for a certain property: this is required, automatically create a constructor that instantiates this object

### 2.4 Repositories
after creating the domain objects, need a mechanism to get data in and out of our database
we have hibernate under the hood and we're using Aughrim to map our objects
establish Repository package, touch interface files for each entity, write in it:
public interface LinkRepository extends JpaRepository<Link,Long> {
}
We don't need to implement the repository interface, spring does it at runtime

### 2.5 Entity Relationships (Mappings)
Use JPA Mapping Annotations
@OneToMany(mappedBy = "link") 1 link has many 

### 2.6 Auditing Aware
some features: time for create and update, who create or update
a bunch of classes needs to be auditable, we can create an abstract class called Auditable and make it a mappeed super class, just extend
this class for those classes needing auditable features. This Auditable class doesn't not have a table, the auditable features are within 
the domain object

## 3. Database Layer

### 3.1 Common Application Properties & H2 Database
Data sourse: spring.datasource.[data-username/password...]
Web console: link to H2 in-memory database

connect to h2
h2 datasource setting in application.properties:
spring.datasource.url=jdbc:h2:mem:springit
spring.datasource.username=sa
spring.datasource.password=

### 3.2 connect to Mysql
spring.jpa.hibernate.ddl-auto=create
spring.datasource.url=jdbc:mysql://localhost:3306/springit?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=springit
spring.datasource.password=
in mysql workbench:
add user, give privilege
execute:
use springit;
select * from link/comment/vote

### 3.3 Database Schema & Data
turn on in application.properties
spring.datasource.initialization-mode=always
then add in resources: schema.sql for creating/dropping the db, data.sql for writting some data into the db

### 3.4 Command line runner (interface)
first method:
2 CLRs, 1 for database initialization, the other for doing something else, all in com.lunz.springit
use @Order(num) to schedule the order for these 2 CLRs

second method (for this project):
use bean in SpringitApplication

### 3.5 explore repository
define a method in repository just follows the query scheme
e.g. Link findByTitle(String title) in LinkRepository

## 4. Spring MVC: Controller
### 4.1 what's is controller
handles HTTP request and mapping
the controller package should be under the main folder
use @Controller for match template html file in resources/templates
use @RestController without a template
@RequestMapping(urlpath) to map the http request to the method
@GetMapping = @RequestMapping + GET

### 4.2 Handler method
pass data down to our view
@Controller
public class HomeController {
@GetMapping("/home_page") // the url for mapping
public String home(Model model, HttpServletRequest request){
model.addAttribute("title","Hello, Thymeleaf!");
return "home"; // "home.html" template
}
}

### 4.3 Link controller


## 5. Spring MVC: View
### 5.1 Spring MVC: View (The User Interface)
Welcome page
Spring Boot serves static content from a directory called /static (or /public or /resources or /META-INF/resources) in the classpath
it firstly looks for index.html in above static content, it's automatically used as the welcome page if found

Custom Favicon
favicon.ico in the configured static content
website favicon.io for making favicons using png figure
Note:!!!
placing the figure: note the path shouldn't include special chars
favicon.ico didn't work...

### 5.2 Templates
ViewResolver
template engine: Thymeleaf

use the data attribute for displaying dynamic features:
display the title: <h1 data-th-text="${title}">Default Welcome Message</h1>

### 5.3 Thymeleaf Layouts
read the head from database, add them as model attributes and pass them down to the templates
for most of the pages we have same header
<title th:text="${title}">Springit - Spring Boot Reddit Clone</title> the default title when no title attribute passed from controller
<head th:replace="~{layouts/main_layout :: head(title = ${pageTitle})}">, the pageTitle is passed down to the layout through attribute addition of "title" in linkController

### 5.4 List Links (Home)
save a bunch of links (title and url) to linkRepository in bootstrap/DatabaseLoader
Pretty time - library for recording precice time
in html, ${link.id} this helper method just call a link domain object

comment,username -> Spring security

### View Link
if (link.isPresent()) {
model.addAttribute("link",link.get());
return "link/view";
} else {
return "redirect:/";
}

### 5.5 Submit Link
2 handler method: one to show the page, one to handle the form submission
validation: in the domain object class:
@NotEmpty(message = "Please enter a url.")
@URL(message = "Please enter a valid url.")

## 6. Spring Security: Configuration

### 6.1 Introduction
spring boot 2 avoids the orders of configuration for security
once add the spring-boot-security dependency, by default we will access the login page (everything else is locked down by this page), but we can use the security password in generated console to log in

### 6.2 Configuration
http
.authorizeRequests()
.antMatchers("/").permitAll() //everyone has access to home
.antMatchers("/link/submit").hasRole("ADMIN"); // only ADMIN has access to submit page
.and()
.formLogin(); //enable log in to /submit through a login page, still needs the role

in application.properties, spring.security.user.roles=ADMIN
Note: if change the access and it can't take effect on the browser, clear the coookies in 开发者工具/Cookies

### 6.3 Users & Roles
authentication
create a User class in domain/, limit the size and uniqueness of username and password

authorization: Role

### 6.4 User Details Service
override the configure method from our WebSecurityConfigurerAdapter in security/SecurityConfiguration
then actually create the implementation in security/UserDetailsServiceImpl

### 6.5 Add Users & Roles to Database
password encryptor - use Bcrypt by Spring
Repository stores the encrypted password, but we can use the actual password to login

### 6.6 Auditing Configuration
(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()) return the current user
Note: if we put some existing rows to the repo, remember they don't have login user, so it can't be got by auditing, the resolution is to return a default email if there is no login user

### 6.7 Actuator Security
http
.requestMatchers(EndpointRequest.to("info")).permitAll() // easy way to get to that endpoint
.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
.antMatchers("/actuator/").hasRole("ACTUATOR")

management.endpoint.health.show-details=when_authorized
so in /actuator, the info endpoint is open to everyone, but other endpoints need authorization to access

### 6.8 H2-console security
let anyone to access /h2-console/**

## 7. Spring Security: The View Layer

### 7.1 introduction
current login form: default from spring security
now create a custom login form - spring security 

### 7.2 custom login form
new controller to map "templates/auth/login.html"

### 7.3 Username & Password Form Parameters
http.usernameParameter("email");
templates/auth/login.html

### 7.4 Logout
after adding CSRF, we can only use HTTP post to log out
type an URL in the address bar, this is a GetMapping, can't allow us to log out
make a change in the main layout to use a post instead of a get
http.logout();

### 7.5 Remember Me
send a cookie to the browser for automatic login
2 implementations
(1) hashing to preserve the security of cookie-based tokens (our method)
(2) use database or other persistent storage to store generated tokens
2 cookies
(1)JsessionID
(2)rememberme
http.rememberMe()

### 7.6 Thymeleaf Spring Security Dialect
the Spring Security integration module works as a replacement of the Spring security taglib
The sec:authorize attribute renders its content when the attribute expression is evaluated to true
use dialect to seperate the content for login and logout state
sign in -> submit, account, sign-out
sign out -> sign-in, register

The sec:authorize attribute renders its content when the attribute expression is evaluated to true:

<div sec:authorize="isAuthenticated()">
  This content is only shown to authenticated users.
</div>
<div sec:authorize="hasRole('ROLE_ADMIN')">
  This content is only shown to administrators.
</div>
<div sec:authorize="hasRole('ROLE_USER')">
  This content is only shown to users.
</div>
The sec:authentication attribute is used to print logged user name and roles:

Logged user: <span sec:authentication="name">Bob</span>
Roles: <span sec:authentication="principal.authorities">[ROLE_USER, ROLE_ADMIN]</span>

### 7.7 Who submitted the link?
set up a connect between the user and link class
update 2 places:
(1) list.html
(2) view.html
class="author" th:text="${link.createdBy}">Lunz

### 7.8 Account and Registration
/profile -> account
/register -> register
getmapping + load templates

## 8. Voting

### 8.1 Up Vote & Down Vote Intro
score = upvotes - downvotes

### 8.2 Vote Entity & Repository

short direction in Vote; +1/-1

@ManytoOne
List<Vote> votes;
int voteCount in Link;
each time update the voteCount in our db

### 8.3 Voting Controller
@GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
get the link by ID through linkRepository
create a new Vote object with direction defined and save it to voteRepository
update the linkCount in object link and save it to linkRepository

### 8.4 Calling our Vote API
call the vote controller method through clicking an area
add a script in templates/link/list.html:
select all upvote and downvote classes, apply event listener ('click')
get id for link
get direction from the up/down vote class
get voteCount from link object through linkid
url = combination of linkid, direction, voteCountvalue
call our votecontroller API through fetch(url)
print the voteCountValue to the screen

### 8.5 security
not login but click up/down vote - cause login through "anonymous", add that condition to AuditorAwareImpl
to do:
(1) user interface side, somebody can't vote if they don't log in
Thymeleaf Spring Security Dialect
// <script sec:authorize="hasRole('USER')">
as long as a user is authenticated, the script would be executed
then if not login and click the vote, there is no eventlistener
but this can't protect the url

(2) @Secured({"ROLE_USER"}) before getmapping in votecontroller

## 9. Comments

### 9.1 Database Loader: Add new comments
add comments to db in commandline runner (Dbloader)

### 9.2 List Comments
add getPrettytime in Comment domain
loop to display each comment in templates/link/view.html

### 9.3 Add New Comment
the user need the USER_ROLE to see the add comment option
like submitting new link, need to @PostMapping("/link/comments") to get the binding result from view.html into our repo in linkController

