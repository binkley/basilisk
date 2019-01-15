<img src="basil-lips.jpg" alt="Image of basil" align="right"/>

# Basilisk

Demonstrate Java 11, Spring Boot 2, JUnit 5, et al

## Features

* Modern Spring Boot
* Pure service layer
* Repository
* Validations
* YAML configuration
* Full test coverage
* Static code analysis

## Feedback

_Please_ file GitHub issues for questions, suggestions, additions or 
improvements! 

## Examples

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
