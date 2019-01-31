<img src="basil-lips.jpg" alt="Image of basil lips" align="right"/>

# Basilisk

Demonstrate Java 11, Spring Boot 2, JUnit 5, et al

* [Features](#features)
* [Feedback](#feedback)
* [Production](#production)
* [Development](#development)
* [Testing](#testing)
* [Advice and examples](#advice-and-examples)


## Features

* Modern Spring Boot
* Spring Data JDBC
* Incremental build
* Build dashboard
* Pure service layer
* Schema and data migrations
* YAML configuration
* Validations
* RFC 7807 (problem+json)
* Strict, fail-fast compilation
* Full test coverage
* Static code analysis
* Build analysis


## Feedback

_Please_ file GitHub issues for questions, suggestions, additions or
improvements!


## Production

### Actuator

After spinning up the program with `./gradlew bootRun`, try
[actuator](http://localhost:8080/actuator).  The page format is
[JSON HAL](http://stateless.co/hal_specification.html), so browse the
returned JSON for interesting links.

Of particular interest is [health](http://localhost:8080/actuator/health).
Note the [application configuration](src/main/resources/application.yml) to
expose more detail.

### Database

Production uses PostgreSQL; lower environments use H2 (in-memory).  See
[`application.yml`](src/main/resources/application.yml).  An example for the
command line:

```bash
./gradlew bootRun -Dspring.profiles.active=production
```

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

### Dependencies

Periodically check for updates to gradle, plugins, and dependencies with
`./gradlew dependencyUpdates`; this prints a report to console, and to
`./build/dependencyUpdates/report.txt`.  (Unfortunately, this plugin does not
tie into the build dashboard.)

### Running

Bring up the program with:

* Command line &mdash; `./gradlew bootRun`
* IntelliJ &mdash; run/debug/profile
[the application](src/main/java/hm/binkley/basilisk/BasiliskApplication.java)

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
  under [`src/test`](src/test); run with `./gradlew test`
* Integration tests &mdash; Spring wiring is used.  These go under
  [`src/integrationTest`](src/integrationTest); run with `./gradlew
  integrationTest`
* Database tests &mdash; In addition to Spring wiring, these use a database
  resource.  These go under [`src/databaseTest`](src/databaseTest); run with
  `./gradlew databaseTest`
* Live tests &mdash; The entire application is wired and brought up, the
  most rare and expensive kind of tests.  These go under
  [`src/liveTest`](src/liveTest); run with `./gradlew liveTest`

To run all test types, use `./gradlew check`.  To refresh the build, and force
all tests to re-run, use `./gradlew clean check --no-build-cache`.

[`BasiliskServiceTest`](src/test/java/hm/binkley/basilisk/service/BasiliskServiceTest.java)
(unit) and
[`BasiliskServiceValidationTest`](src/integrationTest/java/hm/binkley/basilisk/service/BasiliskServiceValidationTest.java)
(integration) are an example of splitting testing of a class to limit
resources, and speed up the tests.

In this project, the database is an in-memory H2 instance, so is
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

### Database tests

Spring blogs on Spring Data JDBC domain relationships in
[_Spring Data JDBC, References, and Aggregates_](https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates).

- [`BasiliskRepositoryTest`](src/databaseTest/java/hm/binkley/basilisk/store/BasiliskRepositoryTest.java)
  tests a simple model with no references to other domain objects
- [`OneToOneRepositoryTest`](src/databaseTest/java/hm/binkley/basilisk/store/OneToOneRepositoryTest.java)
  tests a one-to-one domain model
- [`ManyToOneRepositoryTest`](src/databaseTest/java/hm/binkley/basilisk/store/ManyToOneRepositoryTest.java)
  tests a many-to-one domain model
- [`ManyToManyRepositoryTest`](src/databaseTest/java/hm/binkley/basilisk/store/ManyToManyRepositoryTest.java)
  tests a many-to-many domain model


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

- [configuration](src/main/java/hm/binkley/basilisk/configuration)
- [endpoints](src/main/java/hm/binkley/basilisk/rest)
- [persistence](src/main/java/hm/binkley/basilisk/store)
- [services](src/main/java/hm/binkley/basilisk/service)

Recall that package names are stylistically singular, not plural, _eg_,
`service` rather than `services`.

### Test types

- [application (live)](src/liveTest/java/hm/binkley/basilisk/BasiliskApplicationTest.java)
- [configuration (unit)](src/test/java/hm/binkley/basilisk/configuration/BasiliskConfigurationTest.java)
- [controller (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskControllerTest.java)
- [controller validation (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskControllerValidationTest.java)
- [json request (unit)](src/test/java/hm/binkley/basilisk/rest/BasiliskRequestTest.java)
- [json response (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskResponseTest.java)
- [properties (integration)](src/integrationTest/java/hm/binkley/basilisk/configuration/BasiliskPropertiesTest.java)
- [record validation (unit)](src/test/java/hm/binkley/basilisk/store/BasiliskRecordValidationTest.java)
- [repository (database)](src/databaseTest/java/hm/binkley/basilisk/store/BasiliskRepositoryTest.java)
- [service (unit)](src/test/java/hm/binkley/basilisk/service/BasiliskServiceTest.java)
- [service validation (integration)](src/integrationTest/java/hm/binkley/basilisk/service/BasiliskServiceValidationTest.java)

Note the source root of each test depends on the resources it uses.  See
[Testing - Layout](#layout).  Also note the prevalence of integration
tests: this is a common drawback to Spring projects.

#### Controller tests

Two kinds of help in this project for JSON-based REST endpoints:

- [happy path](src/integrationTest/java/hm/binkley/basilisk/configuration/JsonWebMvcTest.java)
- [sad path](src/integrationTest/java/hm/binkley/basilisk/configuration/ProblemWebMvcTest.java)

These replace Spring `@WebMvcTest` annotation to ensure JSON sent and 
received.

### Spring configuration

Keep your top-level application class simple, generally just a `main()`
which calls `SpringApplication.run(...)`.  Provide a top-level
configuration class, initially empty.  On the configuration class go any
`@Enable*`-type Spring annotations, not on the application class.  
Specialize your configuration classes as makes sense.

See:

- [`BasiliskApplication`](src/main/java/hm/binkley/basilisk/BasiliskApplication.java)
- [`BasiliskConfiguration`](src/main/java/hm/binkley/basilisk/configuration/BasiliskConfiguration.java)
- [`ProblemConfiguration`](src/main/java/hm/binkley/basilisk/configuration/ProblemConfiguration.java)
- [`SecurityConfiguration`](src/main/java/hm/binkley/basilisk/configuration/SecurityConfiguration.java)
- [`SwaggerConfiguration`](src/main/java/hm/binkley/basilisk/configuration/SwaggerConfiguration.java)

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
[`BasiliskController`](src/main/java/hm/binkley/basilisk/rest/BasiliskController.java),
[`BasiliskProperties`](src/main/java/hm/binkley/basilisk/configuration/BasiliskProperties.java),
and
[`BasiliskService`](src/main/java/hm/binkley/basilisk/service/BasiliskService.java).

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

- [`BasiliskProperties`](src/main/java/hm/binkley/basilisk/configuration/BasiliskProperties.java)
- [`OverlappedProperties`](src/main/java/hm/binkley/basilisk/configuration/OverlappedProperties.java)

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
  [`BasiliskPropertiesTest`](src/integrationTest/java/hm/binkley/basilisk/configuration/BasiliskPropertiesTest.java)
- `@DataJdbcTest`; example in
  [`BasiliskRepositoryTest`](src/databaseTest/java/hm/binkley/basilisk/store/BasiliskRepositoryTest.java)
- `@WebMvcTest` (use the `value` property to limit test to one controller);
  example in
  [`BasiliskControllerTest`](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskControllerTest.java)

### Configuration through annotations

Spring makes heavy use of configuration in Java through annotations.  Examples
include `@EnableConfigurationProperties` and `@Import`.  See
[`JsonWebMvcTest`](src/integrationTest/java/hm/binkley/basilisk/configuration/JsonWebMvcTest.java)
for an example of writing your own.
