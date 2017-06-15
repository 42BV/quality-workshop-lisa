


```
docker run --rm --tty -e POSTGRES_PASSWORD=postgres -p 5432:5432 --name postgression postgres:9.6
```

Errors to run into:

1. Run the application and therefore the Liquibase scripts. Notice that something is still missing and change the Liquibase script:

```
Caused by: liquibase.exception.ValidationFailedException: Validation Failed:
     1 change sets check sum
          db/changelogs/changelog_000.xml::1::bas.de.vos@42.nl was: 7:0800ed5a942c97ec4ad6b2912c86f09a but is now: 7:54a6c70c5c8960704c8ac08cd2bb3b49
```

2. 