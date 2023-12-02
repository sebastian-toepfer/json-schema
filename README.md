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
