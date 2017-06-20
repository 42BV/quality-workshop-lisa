

## Various run modes supported
* Instant Developer Experience
* Unit tests
* Against the real IMDB
* Against a Wiremock proxy for the real IMDB

### Instant Developer Experience
When the project is checked out and ran out-of-the-box, it will use the Wiremock stubs by default.

```
mvn spring-boot:run
```

### Unit tests
The unit tests run completely on the Wiremock stubs, ensuring a reliable set of request/response pairs to work with.

```
mvn clean install
```

### Against the real IMDB
You can run against the real IMDB, completely bypassing the Wiremock stubs.

```
mvn spring-boot:run -Dspring.profiles.active=imdb
```

### Against a Wiremock proxy for the real IMDB
This run mode is to create new request/response pairs for your stubs.

Make sure you have a standalone Wiremock server set up as per the instructions below 'Record wiremock stubs'.

```
mvn spring-boot:run -Dspring.profiles.active=record
```

## Record wiremock stubs
Roodkopwever's main purpose is to visualize crowd's group structure. In order to do this it asks
crowd for its groups. The instant developer experience and the tests make use of wiremock stubs
to simulate the crowd connection. The process to record the stubs is described below.

Remove the following two folders:
```
src/test/resources/mappings
src/test/resources/__files
```

Start the wiremock server as a standalone process. Make sure the server proxies crowd and 
records all the request-response pairs:
```
cd src/test/resources
java -jar wiremock-standalone-2.6.0.jar --proxy-all="http://www.imdb.com" --record-mappings --verbose --port 8089
```

Start the application with the crowd/record profiles. The crowd profile tells the application where to proxy to.
The record profile makes sure that the wiremock server is called with crowd as its proxy:
```
mvn spring-boot:run -Dspring.profiles.active=record
```

Add the following two folders to git:
```
src/test/resources/mappings
src/test/resources/__files
```
