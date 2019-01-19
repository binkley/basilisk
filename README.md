<img src="basil-lips.jpg" alt="Image of basil lips" align="right"/>

# Basilisk

Demonstrate Java 11, Spring Boot 2, JUnit 5, et al

## Features

* Modern Spring Boot
* Incremental build
* Build dashboard
* Pure service layer
* Repository
* YAML configuration
* Validations
* Strict, fail-fast compilation
* Full test coverage
* Static code analysis
* Build analysis

## Feedback

_Please_ file GitHub issues for questions, suggestions, additions or
improvements!

## Getting around

### Command line

Build the project, run tests, generate reports, and package program:
```bash
./gradlew
```

Run the program:
```bash
./gradlew bootRun
```

Examine at all reports (use "firefox" instead of "open" if your machine does
not have the "open" command):
```bash
(cd build/reports/buildDashboard; open index.html)
```

###

## Production

### Actuator

After spinning up the program with `./gradlew bootRun`, try
[actuator](http://localhost:8080/actuator).  The page format is
[JSON HAL](http://stateless.co/hal_specification.html), so browse the 
returned JSON for interesting links.

Of particular interest is [health](http://localhost:8080/actuator/health). 
Note the [application configuration](src/main/resources/application.yml) to
expose more detail.

### Swagger

Of course, there is a [Swagger UI](http://localhost:8080/swagger-ui.html) 
to browse your REST endpoints.

### Spring Data REST

You can browse [the Spring repository](http://localhost:8080/data) with a 
nice web interface.

## Development

Yes, there is [Swagger](http://localhost:8080/swagger-ui.html).

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
most expensive kind of tests.  These go under [`src/liveTest`]
(src/liveTest); run with `./gradlew liveTest`

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

## Advice and examples

_NB_ &mdash; Anything mentioned as a "bean" means anything that Spring DI
creates and/or injects.  Spring is very flexible about this; in most cases
beans are instances of classes.

### Build

See what tasks are run, and their dependencies with `./gradlew <tasks>
taskTree` (append `taskTree` after the list of tasks to analyze).

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
- [controller (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskControllerTest.java)
- [controller validation (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskControllerValidationTest.java)
- [json request (unit)](src/test/java/hm/binkley/basilisk/rest/BasiliskRequestTest.java)
- [json response (integration)](src/integrationTest/java/hm/binkley/basilisk/rest/BasiliskResponseTest.java)
- [properties (integration)](src/integrationTest/java/hm/binkley/basilisk/configuration/BasiliskPropertiesTest.java)
- [record validation (unit)](src/test/java/hm/binkley/basilisk/store/BasiliskRecordValidationTest.java)
- [repository (database)](src/databaseTest/java/hm/binkley/basilisk/store/BasiliskRepositoryTest.java)
- [service (unit)](src/test/java/hm/binkley/basilisk/service/BasiliskServiceTest.java)
- [service validation integration](src/integrationTest/java/hm/binkley/basilisk/service/BasiliskServiceValidationTest.java)

Note the source root of each test depends on the resources it uses.  See 
[Testing - Layout](#layout).  Also note the prevalence of integration 
tests: this is a common drawback to Spring projects.

### Spring configuration

Keep your top-level application class simple, generally just a `main()`
which calls `SpringApplication.run(...)`.  Provide a top-level
configuration class, initially empty.  On the configuration class go any
`@Enable*`-type Spring annotations, not on the application class.  See:

- [`BasiliskApplication`](src/main/java/hm/binkley/basilisk/BasiliskApplication.java)
- [`BasiliskConfiguration`](src/main/java/hm/binkley/basilisk/configuration/BasiliskConfiguration.java)

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

Any bean can be validated by adding `@Validated` to the class.  See
examples of
[`BasiliskController`](src/main/java/hm/binkley/basilisk/rest/BasiliskController.java),
[`BasiliskProperties`](src/main/java/hm/binkley/basilisk/configuration/BasiliskProperties.java),
and
[`BasiliskService`](src/main/java/hm/binkley/basilisk/service/BasiliskService.java).

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
