# Milestone 02
The application now relies on an external Postgres instance. Make sure that we have Dockerized Postgres which starts regardless of run-mode.

## System requirements
Make sure the following tools are installed on the system:
* Maven v3+
* Java 8+
* Docker

## Steps

1. Add dependencies to the POM:

* Versions
```xml
<spring-boot-docker-postgres.version>0.7.0</spring-boot-docker-postgres.version>
<spring-boot-starter-docker.version>0.7.0</spring-boot-starter-docker.version>
```

* Dependencies

```xml
<dependency>
    <groupId>nl.42</groupId>
    <artifactId>spring-boot-starter-docker</artifactId>
    <version>${spring-boot-starter-docker.version}</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>nl.42</groupId>
    <artifactId>spring-boot-docker-postgres</artifactId>
    <version>${spring-boot-docker-postgres.version}</version>
    <scope>provided</scope>
</dependency>
```

2. Configure application.yml:

```yaml
docker:
  postgres:
    image-version: 9.6
```

3. Execute the local run:

```
mvn spring-boot:run
```

4. Start a pgadmin console to check the content of the database. 