# Milestone 01
Replace H2 with Postgres.

## System requirements
Make sure the following tools are installed on the system:
* Maven v3+
* Java 8+
* Docker

## Steps

1. Run the unit tests; see they all work. These tests were run on H2. 
  
2. Replace the H2 dependency with a Postgres dependency:

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>provided</scope>
</dependency>
```

3. Configure the Postgres datasource:

```yaml
spring:
  datasource:
    platform: postgresql
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: postgres
```

4. Run a Docker Postgres container

```
docker run --rm --tty -e POSTGRES_PASSWORD=postgres -p 5432:5432 --name postgression postgres:9.6
```

5. Run the unit tests once more. See two failures:
* table 'user' is not allowed in Postgres
* Postgres JDBC driver cannot deal with null value supplied to upper() method

6. Fix the two issues:
* rename 'user' to 'app_user', both in ```User``` and ```changeset_000.xml```
* replace ```CollectorsItemRepository.findUpperEmail``` with ```findByEmailIgnoreCase```.

7. Re-run the unit tests. Note how Liquibase refuses to run, because the hash of the changeset no longer matches the stored on. 
* stop Docker Postgres

8. Re-run the unit tests. Note how no Postgres database is available.
* start Docker Postgres

8. Re-run the unit tests. All tests now pass.