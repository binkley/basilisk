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

(Jump down [past "Model"](#features), to features, testing, etc.  Model is
under construction.)

## Model

### Basil Lips B&Bs

Picture that you own a chain of Quality B&Bs (bed and breakfast
hotels)&mdash;_The Basil Lips_&mdash;following a flower theme, and located
around the globe:

- ["The Dallas Yellow Rose"](https://www.history.com/news/who-was-the-yellow-rose-of-texas)
- ["Melbourne's Pink Heath"](https://www.anbg.gov.au/emblems/vic.emblem.html)
- ["Cherry Blossom of Tokyo"](https://www.jrailpass.com/blog/cherry-blossoms-tokyo)

<img src="https://www.featurepics.com/StockImage/20070125/yellow-rose-of-texas-stock-image-200039.jpg"
width="30%"/>
<img src="https://jpmbooks.files.wordpress.com/2012/06/img_0185.jpg?w=1000&h=1332"/>
width="30%"/>
<img src="https://cdn-images-1.medium.com/max/2000/1*C2nsRby3rpDCA5LFWfDZUw.jpeg"
width="30%"/>

Your business plan includes jet-setting world-class chefs and locally-sourced
ingredients.

### The Food

### The Rules

1. Chefs fly between your locations, delighting your guests with their amazing
   cooking.
2. Chefs use nearly all special ingredients in one of their signature recipes.
3. Ingredients for recipes can vary, as chef's make them their own.
4. Ingredients often location-specific, and are at their best only certain
   times of the year.
5. You are still a business; best is to use what ingredients are present in
   each location: your buyers are some of the best in the world, like your
   chefs.

#### Example recipes

- A perfect souffle needs eggs, and other wet and dry goods, and varies widely
  in those other ingredients

#### Example ingredient restrictions

- [Eggs](https://en.wikipedia.org/wiki/Egg_as_food) are available in all
  locations at all times
- The [Tokyo fish market](https://en.wikipedia.org/wiki/Tsukiji_fish_market)
  has the best fish in the world, year-round
- [Texas strawberries](https://strawberryfestival.com) are best only in
  Dallas, and only in Spring

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

* [`Recipe`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/domain/Recipe.java)
  is an aggregate root
* [`Ingredient`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/domain/Ingredient.java)
  is a kind of root as well, in that unused ingredients are important, but
  ingredients can also belong to a recipe as well, as your "meal planners"
  schedule stock for each location and season
* [`Source`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/domain/Source.java)
  defines ingredients, in a similar fashion to classes in Java define
  instances; it is its own aggregate root
* [`Chef`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/domain/Chef.java)
  is important, but _not_ an aggregate root.  You move chefs around your
  locations over time, but the recipes are tied more to ingredients, and
  possibly restricted by location and season

### Sort of a picture (if you turn the page sideways and squint)

These are the most basic aggregates, what is most important in pleasing your
guests and helping the "meal planners":

- RECIPE <-- INGREDIENTs <-- RESTRICTIONs (consigned ingredients)
- INGREDIENTs <-- RESTRICTIONs (available ingredients)

These are "2nd-class objects", needed by "meal planners", but not as central
(but still important):

- CHEF
- LOCATION
- SEASON


## Features

* [Docker](#docker)
* Modern Spring Boot
* [Spring Boot Actuator](#actuator)
* [Spring Boot Admin](#admin)
* Spring Data JDBC
* [Spring Data JDBC auditing](#spring-data-jdbc-auditing)
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
* [No embedded JSON in tests](#json-test-files)
* Static code analysis
* Build analysis
* YAML Spring Cloud Contract tests
* [Custom JSON formatting](#custom-json-formatting)
* Semantic UI CSS-only for home page
* [Custom health check](#custom-health-check)
* [Quieter Spring Boot tests](#quieter-spring-boot-tests)
* [Spring Data JDBC upsert](#spring-data-jdbc-upsert)


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


## Docker

### Plain docker

Use the [`Dockerfile`](Dockerfile) to both _build_ and _run_ the program.  The
[`run-it.sh`](run-it.sh) script makes this easy:

```bash
run-it.sh
```

### Docker compose

Use the [`docker-compose.yml`](docker-compose.yml) to _build_ and _run the
program, including a Postgres.

## Production

[Start the program](#running), then try
[the Home Page](http://localhost:8080).

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

Alternatively, develop in [Docker](#docker). 

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

[`SpecialServiceTest`](basilisk-service/src/test/java/hm/binkley/basilisk/flora/service/SpecialServiceTest.java)
(unit) and
[`SpecialServiceValidationTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/service/SpecialServiceValidationTest.java)
(integration) are an example of splitting testing of a class to limit
resources, and speed up the tests.

In this project, the database is an in-memory PostgreSQL instance, so is
self-contained and speedy; however, in production projects, it would be an
external database process.

### JSON test files

Rather than use embedded JSON strings in Java tests, throughout is used JSON
test files, for example, 
[`chef-response-test.json`](basilisk-service/src/integrationTest/resources/hm/binkley/basilisk/flora/rest/chef-response-test.json).
The rules of thumb for these:

1. Place the JSON test file in the same package as the test class, but under
   `resources` rather than `java` directory
2. Name the JSON test file after the Java test class

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

- [`RecipeRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/flora/domain/store/RecipeRepositoryTest.java)
  tests [the sample domain model](#model)
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
  [the contract test](basilisk-contracts/build/generated-test-sources/contracts/hm/binkley/basilisk/contracts/FloraTest.java)
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
- [endpoints](basilisk-service/src/main/java/hm/binkley/basilisk/flora/rest)
- [persistence](basilisk-service/src/main/java/hm/binkley/basilisk/flora/domain/store)
- [services](basilisk-service/src/main/java/hm/binkley/basilisk/flora/service)

Recall that package names are stylistically singular, not plural, _eg_,
`service` rather than `services`.

### Test types

- [application (live)](basilisk-service/src/liveTest/java/hm/binkley/basilisk/BasiliskLiveTest.java)
- [application (contract)](basilisk-contracts/src/test/resources/contracts/flora/A_see_no_chefs.yml)
- [configuration (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/configuration/JsonConfigurationTest.java)
- [configuration (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/configuration/PropertiesConfigurationTest.java)
- [controller (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/rest/RecipeControllerTest.java)
- [controller validation (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/rest/RecipeControllerValidationTest.java)
- [json request (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/flora/rest/RecipeRequestTest.java)
- [json response (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/rest/RecipeResponseTest.java)
- [record validation (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/flora/domain/store/RecipeRecordValidationTest.java) (TODO)
- [repository (database)](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/flora/domain/store/RecipeRepositoryTest.java)
- [service (unit)](basilisk-service/src/test/java/hm/binkley/basilisk/flora/service/SpecialServiceTest.java)
- [service validation (integration)](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/service/SpecialServiceValidationTest.java)

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
[`RecipeController`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/rest/RecipeController.java),
[`FloraProperties`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/configuration/FloraProperties.java),
and
[`SpecialService`](basilisk-service/src/main/java/hm/binkley/basilisk/flora/service/SpecialService.java).

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
  [`RecipeRepositoryTest`](basilisk-service/src/databaseTest/java/hm/binkley/basilisk/flora/domain/store/RecipeRepositoryTest.java)
- `@WebMvcTest` (use the `value` property to limit test to one controller);
  example in
  [`RecipeControllerTest`](basilisk-service/src/integrationTest/java/hm/binkley/basilisk/flora/rest/RecipeControllerTest.java)

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

### Spring Data JDBC auditing

See
[`DatabaseConfiguration`](basilisk-service/src/main/java/hm/binkley/basilisk/configuration/DatabaseConfiguration.java).

### Spring Data JDBC upsert

See
[`StandardRepository`](basilisk-service/src/main/java/hm/binkley/basilisk/store/SeasonRepository.java).
