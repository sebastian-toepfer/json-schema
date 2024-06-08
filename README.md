![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/sebastian-toepfer_json-schema?server=https%3A%2F%2Fsonarcloud.io)
![Sonar Tests](https://img.shields.io/sonar/tests/sebastian-toepfer_json-schema?server=https%3A%2F%2Fsonarcloud.io)
![Sonar Violations](https://img.shields.io/sonar/violations/sebastian-toepfer_json-schema?server=https%3A%2F%2Fsonarcloud.io)

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/sebastian-toepfer/json-schema/build.yml)

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.sebastian-toepfer.json-schema/json-schema)
![GitHub Release](https://img.shields.io/github/v/release/sebastian-toepfer/json-schema)
![GitHub commits since latest release](https://img.shields.io/github/commits-since/sebastian-toepfer/json-schema/latest)

[![Reproducible Builds](https://img.shields.io/badge/Reproducible_Builds-ok-success?labelColor=1e5b96)](https://github.com/jvm-repo-rebuild/reproducible-central/blob/master/content/io/github/sebastian-toepfer/json-schema/json-schema/README.md)

# json-schema
json schema for json-api

## maven dependency

```xml
<!-- api only -->
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>json-schema-api</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
<!-- default impl. -->
<dependency>
  <groupId>${project.groupId}</groupId>
  <artifactId>json-schema-core</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## create a jsonschema instance
```java
final JsonSchema schema = JsonSchemas.load("{}");
```

## validate a json instance againts a schema
```java
if (schema.validator().isValid("{}")) {
  processJson("{}");
} else {
  throws new IllegalArgumentException("Json is not valid");
}
```

## extend with own keywords (define a vocabulary)

Provide a service of type `io.github.sebastiantoepfer.jsonschema.vocabulary.spi.LazyVocabularies`  
Provide a implemention of type `io.github.sebastiantoepfer.jsonschema.Vocabulary`  

To use your new Vocabulary put it into the classpath define it in the schema:
```
{
  $vocabulary: {
    "<id of your vocabulars>": true //or false if is not required -> means validtion works also if it is not in classpath
  }
}
```
