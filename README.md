# Mongo Utils

[![works badge](https://cdn.rawgit.com/nikku/works-on-my-machine/v0.2.0/badge.svg)](https://github.com/nikku/works-on-my-machine)

This utility library makes it easier to use [MongoDB](http://ww.mongodb.org) in your project.


## Dependencies & Requirements

Java 8 is required.

Following direct dependencies are used:

- `org.mongodb:mongo-java-driver:3.1.0`
- `com.fasterxml.jackson.core:jackson-databind:2.6.2`
- `org.reflections:reflections:0.9.10`
- `org.apache.commons:commons-lang3:3.4`
- `com.google.collections:google-collections:1.0`


## Add to your project
The library will be available from http://bintray.com 

Just add

```
compile 'io.seventyone:mongo-utils:0.0.2'
```

to your dependencies.

The code is documented, a real documentation will follow.


## Usage

### Annotations

You can annotate an entity with `@MongoCollection` and/or any number of `@MongoIndex`:

```
@MongoCollection("user")
@MongoIndex(key = "email", unique = true)
@MongoIndex(key = "role")
public class User implements Serializable {
    ...
}
```

See javadoc for more options.


### Auto-Setup

You can auto-setup entities by providing a package:

```
String packageName = "io.seventyone.example.entities";
mongoServiceObject.autoSetup(packageName);
```

The `@MongoCollection` annotation supports `noAutoSetup`.


### Builder

You can either instantiate the implementations directly (not so pretty) or use the provided builders:

```
MongoServiceBuilder
   .start()
   .host("myhost")             // Optional, default: "localhost"
   .port(12345)                // Optional, default: 27017
   .host("myhost", 12345)      // If you want to set it both in one call
   .database("mydatabase")     // Required
   .credential("user", "pass") // Optional
   .build();
```

```
MongoConverterBuilder
   .start()
   .configure(...)               // Configure the current Jackson ObjectMapper
   .objectMapper(anObjectMapper) // Or provide your own, can be configured afterwards
   .build();
```