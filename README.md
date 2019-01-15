<img src="basil-lips.jpg" alt="Image of basil" align="right"/>

# Basilisk

Demonstrate Java 11, Spring Boot 2, JUnit 5, et al

## Features

* Modern Spring Boot
* Incremental build
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
resource.  These go under [`src/databaseTest`](src/databaseTest); run with`
./gradlew databaseTest`

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

## Advice and examples

_NB_ &mdash; Anything mentioned as a "bean" means anything that Spring DI 
creates and/or injects.  Spring is very flexible about this; in most cases 
beans are instances of classes.

### Build

See what tasks are run, and their dependencies with `./gradlew <tasks> 
taskTree` (append `taskTree` after the list of tasks to analyze).

### Autowiring

Make good use of `@RequiredArgsConstructor(onConstructor = @__(@Autowired))`
and `final` fields in beans.  This saves typing, prevents mistakes in 
tests, and is "best practice" as recommended by Spring documentation.

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
    private SomeBean realBean;
    @MockBean  // Mock instance created by Mockito, and injected by Spring
    private SomeMock mockBean;
    
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
