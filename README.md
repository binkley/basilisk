<img src="basil-lips.jpg" alt="Image of basil lips" align="right"/>

# Basilisk

Demonstrate Java 11, Spring Boot 2, JUnit 5, et al

* [Model](#model)
* [Features](#features)
* [Feedback](#feedback)
* [Production](#production)
* [Development](#development)
* [Testing](#testing)
* [Advice and examples](#advice-and-examples)


## Model

### Basil Lisp B&B Chain

Picture that you own a chain of B&Bs (bed and breakfast) located around the
globe:

- ["The Dallas Yellow Rose"](https://www.history.com/news/who-was-the-yellow-rose-of-texas)
- ["Melbourne's Pink Heath"](https://www.anbg.gov.au/emblems/vic.emblem.html)
- ["Cherry Blossom of Tokyo"](https://www.jrailpass.com/blog/cherry-blossoms-tokyo)

<img src="https://www.featurepics.com/StockImage/20070125/yellow-rose-of-texas-stock-image-200039.jpg"
width="30%"/>
<img src="https://jpmbooks.files.wordpress.com/2012/06/img_0185.jpg?w=1000&h=1332"/>
width="30%"/>
<img src="https://cdn-images-1.medium.com/max/2000/1*C2nsRby3rpDCA5LFWfDZUw.jpeg"
width="30%"/>

### The Food

Your business plan includes good food, not just breakfast, so you hire
several top-shelf chefs, cultivate many impressive recipes, and source the
best ingredients available subject to restrictions.

### The Rules

1. Chefs rotate through your locations, delighting your guests with their
   amazing cooking.
2. Chefs rotate through ingredients, and you would like eventually all chefs
   to try their hand at every ingredient.
3. Ingredients for a recipe can vary somewhat, as substitutions are needed,
   or to create variety.
4. Not all ingredients are available at every location, or at every season.
5. A location may have ingredients available, but not yet consigned to a
   particular recipe.  You would like to use all ingredients on hand, when
   feasible

#### Example recipes

- A perfect souffle needs eggs and some other dry goods, yet varies widely in
  other extra ingredients

#### Example ingredient restrictions

- Eggs are available in all locations at all times
- The Tokyo fish market has the best fish in the world, year-round
- Texas strawberries are best only in Dallas and only in Spring

### Thinking about it

You can reach some conclusions from all this information:

- A recipe is not really tied to a chef, and it its "own thing"
- Some recipes can only be made in some locations, and may depend on season
- Ingredients may be restricted to a location, but many are simply waiting to
  be used
- Unused ingredients are waste, and cost your bottom line
- Location and season are important, but not the "root" of the picture
- Likewise, chefs are not the "root" of the picture, but move around, and
  prepare many, varying meals
- Recipes seem important, and really your concern is pleasing guests

### Implementing

* `Recipe` is an aggregate root
* `Ingredient` is a kind of root as well, in that unused ingredients are
  important, but ingredients can also belong to a recipe as well, as your
  "meal planners" schedule stock for each location and season
* `Chef` is important, but _not_ an aggregate root.  You move chefs around
  your locations over time, but the recipes are tied more to ingredients, and
  possibly restricted by location and season

### Sort of a picture (if you turn the page sideways and squint)

These are the most basic aggregates, what is most important in pleasing your
guests and helping the "meal planners":

- RECIPE <-- INGREDIENTs <-- RESTRICTIONs (consigned ingredients)
- INGREDIENTs <-- RESTRICTIONs (available ingredients)

These are "2nd-class objects", needed by "meal planners", but not as
important:

- CHEF
- RESTRICTION
- LOCATION

Sure, guests care about where they stay, but they are not staying at a
location because of a particular recipe.  However, once there, they care about
having a fabulous meal!


## Features

* Modern Spring Boot
* [Spring Boot Actuator](#actuator)
* [Spring Boot Admin](#admin)
* Spring Data JDBC
* [Swagger](#swagger)
* Incremental build
* Domain-oriented design
* Pure service layer
* Schema and data migrations
* YAML configuration
* Validations
* RFC 7807 (problem+json)
* Strict, fail-fast compilation
* Full test coverage
* Static code analysis
* Build analysis
* YAML Spring Cloud Contract tests
* [Custom JSON formatting](#custom-json-formatting)
* Semantic UI CSS-only for home page
* [Custom health check](#custom-health-check)
* [Quieter Spring Boot tests](#quieter-spring-boot-tests)


## Feedback

_Please_ file [GitHub issues](https://github.com/binkley/basilisk/issues) for
questions, suggestions, additions, bugs, or improvements!

## Design

Demonstrating an alternative to classic Spring "layer cack" design, this
project uses a more domain-oriented approach:

* REST layer with controllers
* Domain objects with business logic
* Pure-function services for non-domain concerns
* Persistence layer with stores
* Strong separation of objects between layers  

Originally, this project followed:

* REST layer with controllers
* Entities which were simply data bags ("anemic object" anti-pattern)
* Service layer with business logic (more "anemic object" anti-pattern)
* Persistence layer with repositories
* Weak separation of objects between layers

Some reading:

* [Anemic Domain Model](https://martinfowler.com/bliki/AnemicDomainModel.html)
* [How accurate is “Business logic should be in a service, not in a model”?](https://softwareengineering.stackexchange.com/questions/218011/how-accurate-is-business-logic-should-be-in-a-service-not-in-a-model)

## Production

Start the program, then try the [Home Page](http://localhost:8080).

### Admin

Try [Spring Boot Admin](http://localhost:8080/admin)
(see [GitHub](https://github.com/codecentric/spring-boot-admin) for an
explanation).

### Actuator

After spinning up the program with `./gradlew bootRun`, try
[actuator](http://localhost:8080/actuator).  The page format is
[JSON HAL](http://stateless.co/hal_specification.html), so browse the
returned JSON for interesting links.

Of particular interest is [health](http://localhost:8080/actuator/health).
Note the [application configuration](basilisk-service/src/main/resources/application.yml) to
expose more detail.

### Database

Production uses external PostgreSQL; lower environments use in-memory
PostgreSQL.  See [`application.yml`](basilisk-service/src/main/resources/application.yml).  An
example for the command line:

```bash
./gradlew bootRun -Dspring.profiles.active=production
```

Note the benefits of embedded PostgreSQL for testing:

* Faster tests, faster build
* Same schema for tests and production (no separate H2 schema)
* Greater discipline for application tests not using the database
* Database accessible externally while debugging tests

### Swagger

Of course, there is a [Swagger UI](http://localhost:8080/swagger-ui.html)
to browse your REST endpoints.  The Swagger REST API endpoint is at the
[usual location](http://localhost:8080/v2/api-docs).

### Spring Data REST

You can browse [the Spring repository](http://localhost:8080/data) with a
nice web interface.


## Development

### Building

Build the project with `./gradlew`.  The default task is "build".

Note the common build configuration for [Spring Boot](spring-boot.gradle),
used in [Basilisk service](basilisk-service/build.gradle).  For example, when
adding Spring Boot Admin, the dependencies go into `spring-boot.gradle` as
they are only for running boot programs.

### Dependencies

Periodically check for updates to gradle, plugins, and dependencies with
`./gradlew dependencyUpdates`; this prints a report to console, and to
`./build/dependencyUpdates/report.txt`.  (Unfortunately, this plugin does not
tie into the build dashboard.)

### Running

Bring up the program with:

* Command line &mdash; `./gradlew bootRun`
* IntelliJ &mdash; run/debug/profile
[the application](basilisk-service/src/main/java/hm/binkley/basilisk/BasiliskApplication.java)

### Reports

To see all build reports, open
[the dashboard](build/reports/buildDashboard/index.html).

### Rest data

Browse and edit the database with
[Spring Data REST](http://localhost:8080/data).


## Testing

### Layout

Divide your tests by what resources they use.  This speeds up testing
individual types of tests:

* Unit tests &mdash; No Spring wiring or other resources needed.  These go
  under [`src/test`](basilisk-service/src/test); run with `./gradlew test`
* Integration tests &mdash; Spring wiring is used.  These go under
  [`src/integrationTest`](basilisk-service/src/integrationTest); run with `./gradlew
  integrationTest`
* Database tests &mdash; In addition to Spring wiring, these use a database
  resource.  These go under [`src/databaseTest`](basilisk-service/src/databaseTest); run with
  `./gradlew databaseTest`
* Live tests &mdash; The entire application is wired and brought up, the
  most rare and expensive kind of tests.  These go under
  [`src/liveTest`](basilisk-service/src/liveTest); run with `./gradlew liveTest`
* Contract tests &mdash; The entire application is wired and brought up, and
  contract tests run against it.  See
  [limitations](#contract-tests). 

To run all test types, use `./gradlew check`.  To refresh the build, and force
all tests to re-run, use `./gradlew clean check --no-build-cache`.

[`BasiliskServiceTest`](basilisk-service/src/test/java/hm/binkley/basilisk/basilisk/service/BasiliskServiceTest.java)
(unit) and
[`BasiliskServiceValidationTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/service/BasiliskServiceValidationTest.java)
(integration) are an example of splitting testing of a class to limit
resources, and speed up the tests.

In this project, the database is an in-memory PostgreSQL instance, so is
self-contained and speedy; however, in production projects, it would be an
external database process.

### Spring injection

Avoid `@InjectMocks`.  It is convenient, but hides wiring mistakes until the
test runs.  Instead, construct your object under test in a setup method,
and mistakes become compilation errors (see example, below).

### Controller tests

The controller tests are straight-forward for Spring projects, if complex
in other contexts.  The exception is testing sad paths.  I never found a
nice way to handle validation failures, nor test test for them.  This is a
long-standing Spring MVC complaint.

Two kinds of help in this project for JSON-based REST endpoints:

- [happy path](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/JsonWebMvcTest.java)
- [sad path](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/ProblemWebMvcTest.java)

These replace Spring `@WebMvcTest` annotation to ensure JSON sent and received.

### Database tests

Spring blogs on Spring Data JDBC domain relationships in
[_Spring Data JDBC, References, and Aggregates_](https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates).

- [`BasiliskRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/basilisk/domain/store/BasiliskRepositoryTest.java)
  tests a simple model with no references to other domain objects
- [`OneToOneRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/store/OneToOneRepositoryTest.java)
  tests a one-to-one domain model
- [`ManyToOneRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/store/ManyToOneRepositoryTest.java)
  tests a many-to-one domain model
- [`ManyToManyRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/store/ManyToManyRepositoryTest.java)
  tests a many-to-many domain model

### Contract tests

To improve the red-green-refactor cycle in IntelliJ for Spring Cloud Contract:

- Defer building and testing to Gradle
- Observe
  [the contract test](basilisk-contracts/build/generated-test-sources/contracts/hm/binkley/basilisk/contracts/BasiliskTest.java)
  and run this directly; Spring Cloud Contract automatically adds this as a
  source root in IntelliJ

Spring Cloud Contract has several limitations, especially the Gradle plugin.
Among them:

- The plugin interacts poorly with Gradle 5 and caching
- Test sources _must_ be underneath `src/test`
- Test code generation is flaky, and does not always produce assertions from
  an input file (eg, `response.matchers.body` generates no code unless there
  is a `response.body`, though the latter is ignored)
- Testing for an empty root array is particularly challenging
- There is little configuration over JSON parsing
- It believes JUnit 5 test-ordering annotations are not yet implemented by
  JUnit


## Advice and examples

_NB_ &mdash; Anything mentioned as a "bean" means anything that Spring DI
creates and/or injects.  Spring is very flexible about this; in most cases
beans are instances of classes.

### Build

See what tasks are run, and their dependencies with
`./gradlew <tasks> taskTree` (append `taskTree` after any list of tasks to
show the tree).

### General layout

Keep your top-level application class in the root of your package hierarchy.
Break up the rest of your classes into categories of related function.  In
this project, there are only four:

- [configuration](basilisk-service/src/main/java/hm/binkley/basilisk/configuration)
- [endpoints](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/rest)
- [persistence](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/domain/store)
- [services](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/service)

Recall that package names are stylistically singular, not plural, _eg_,
`service` rather than `services`.

### Test types

- [application (live)](basilisk-service/src/liveTest/java/hm/binkley/basilisk/BasiliskLiveTest.java)
- [application (contract)](basilisk-contracts/src/test/resources/contracts/basilisk/A_see_no_basilisks.yml)
- [configuration (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/configuration/JsonConfigurationTest.java)
- [configuration (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/PropertiesConfigurationTest.java)
- [controller (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/rest/BasiliskControllerTest.java)
- [controller validation (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/rest/BasiliskControllerValidationTest.java)
- [json request (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/basilisk/rest/BasiliskRequestTest.java)
- [json response (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/rest/BasiliskResponseTest.java)
- [record validation (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/basilisk/domain/store/BasiliskRecordValidationTest.java)
- [repository (database)](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/basilisk/domain/store/BasiliskRepositoryTest.java)
- [service (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/basilisk/service/BasiliskServiceTest.java)
- [service validation (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/service/BasiliskServiceValidationTest.java)

Note the source root of each test depends on the resources it uses.  See
[Testing - Layout](#layout).  Also note the prevalence of integration
tests: this is a common drawback to Spring projects.

### Spring configuration

Keep your top-level application class simple, generally just a `main()`
which calls `SpringApplication.run(...)`.  Provide a top-level
configuration class, initially empty.  On the configuration class go any
`@Enable*`-type Spring annotations, not on the application class.  
Specialize your configuration classes as makes sense.

See:

- [`BasiliskApplication`](basilisk-service/src/main/java/hm/binkley/basilisk/BasiliskApplication.java)
- [`BasiliskConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/BasiliskConfiguration.java)
- [`ProblemConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/ProblemConfiguration.java)
- [`SecurityConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/SecurityConfiguration.java)
- [`SwaggerConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/SwaggerConfiguration.java)

### Autowiring

Make good use of `@RequiredArgsConstructor(onConstructor = @__(@Autowired))`
and `final` fields in beans.  This saves typing, prevents mistakes in
tests, and is "best practice" as recommended by Spring documentation.

This relies on Lombok.  Breaking it down:

- [`@RequiredArgsConstructor`](https://projectlombok.org/features/constructor)
  generates a `public` constructor in the class with all unset `final` fields
  as parameters
- [`onConstructor`](https://projectlombok.org/features/experimental/onX) adds
  additional annotations onto the generated constructor
- `@__(@Autowired)` picks Spring's `@Autowired` annotation for the constructor

The weird "@__" syntax is an artifact of the Java compiler; Lombok has
little other way to express these things in a why which compiles.

```java
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class Foo {
    private final OneThing one;
    private final TwoThing two;
}
```

Becomes (more or less):

```java
class Foo {
    @Autowired
    public Foo(final OneThing one, final TwoThing two) {
        this.one = one;
        this.two = two;
    }
}
```

In IntelliJ, use the "Refactor | Delombok | All annotations" menu item to
see the generated code.  (Do not forget to undo afterwards, to restore the
original, unrefactored code.)

### Bean validation

Any bean can be validated by adding `@Validated` to the class.  See examples
of
[`BasiliskController`](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/rest/BasiliskController.java),
[`BasiliskProperties`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/BasiliskProperties.java),
and
[`BasiliskService`](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/service/BasiliskService.java).

Note: Spring Data JDBC does not support validating entities/records in this
way.  However, a well-written schema will catch issues, and controller and 
service classes should have been validated before attempting to write to 
data store.

### Configuration properties validation

All Spring profiles, active or not, are validated, so this example should not
be included in `application.yml` or no other profile will start:

```yaml

---

spring:
  profiles:
    active: 'broken'

basilisk:
  extra-word: 'F'
```

See:

- [`BasiliskProperties`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/BasiliskProperties.java)
- [`OverlappedProperties`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/OverlappedProperties.java)

### Spring-injected tests

Most of the Spring Boot testing annotations include
`@ExtendsWith(SpringExtension.class)` for you through the magic of Spring
meta-annotations (one exception is `@JsonTest`).

```java
@SomeSpringTestingAnnotation
class SomeTest {
    @Autowired  // Real instance, created and injected by Spring
    private SomeThing realBean;
    @MockBean  // Mock instance created by Mockito, and injected by Spring
    private SomeDependency mockBean;
    @Mock  // Mock instance created by Mockito; ignored by Spring
    private AnotherDependency mockNotBean;
    @SpyBean  // Very rare
    private RealThing spyBean;

    private ClassUnderTest testMe;

    @BeforeEach
    void setUp() {
        testMe = new TestMe(realBean, mockBean);
    }
}
```

Use the Spring Boot annotation _most specific_ to your test.  This limit
Spring to creating/injecting only beans the beans you need, and speeds up
your test.  Among the choices include:

- `@SpringBootTest` (use `classes` property to limit beans created);
  example in
  [`PropertiesConfigurationTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/PropertiesConfigurationTest.java)
- `@DataJdbcTest`; example in
  [`BasiliskRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/basilisk/domain/store/BasiliskRepositoryTest.java)
- `@WebMvcTest` (use the `value` property to limit test to one controller);
  example in
  [`BasiliskControllerTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/basilisk/rest/BasiliskControllerTest.java)

### Configuration through annotations

Spring makes heavy use of configuration in Java through annotations.  Examples
include `@EnableConfigurationProperties` and `@Import`.  See
[`JsonWebMvcTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/JsonWebMvcTest.java)
for an example of writing your own.

### Custom JSON formatting

See
[`JsonConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/JsonConfiguration.java)
for an example of global custom JSON formatting, in this case, showing
`Instant` as, _eg_, "2011-02-03T04:05:06Z" (no milliseconds; UTC timezone),
based on `spring.jackson.date-format` and `spring.jackson.time-zone`
application properties.  Comare to
`@org.springframework.format.annotation.DateTimeFormat`
(which does not handle `Instant`).

### Automatic object lookup in controllers

See
[`BasiliskFromStringIdConverter`](basilisk-service/src/main/java/hm/binkley/basilisk/basilisk/rest/BasiliskFromStringIdConverter.java)
and the `/basilisk/{id}` controller endpoint for an example of Spring
automatically converting types, in this case, from a "string" (a path
component of a URL) to a `BasiliskRecord`. 

### Custom health check

See
[`Happy`](basilisk-service/src/main/java/hm/binkley/basilisk/rest/Happy.java)
as an example custom health check;
[`TimeConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/TimeConfiguration.java)
for setting a default, UTC clock; and
[`HappyTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/rest/HappyTest.java)
for testing time. 

### Quieter Spring Boot tests

See
[`SuppressSpringBootTestsLoggingApplicationListener`](basilisk-testing/src/test/java/hm/binkley/basilisk/SuppressSpringBootTestsLoggingApplicationListenerTest.java)
and matching
[`spring.factories`](basilisk-testing/src/main/resources/META-INF/spring.factories)
for one approach to suppressing "started application" INFO logs during
`@SpringBootTest` tests.
