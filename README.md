# Milestone 03

Add Wiremock to mock the request/responses from IMDB.

## System requirements
Make sure the following tools are installed on the system:
* Maven v3+
* Java 8+
* Docker

## Steps

1. Add the Wiremock dependency to the POM:

```xml
<wiremock.version>2.6.0</wiremock.version>
```

```xml
<dependency>
    <groupId>com.github.tomakehurst</groupId>
    <artifactId>wiremock</artifactId>
    <version>${wiremock.version}</version>
    <scope>provided</scope>
</dependency>
```

2. Create ```nl._42.qualityws.cleancode.config.CollectorProperties```:

```java
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "collector.imdb")
public class CollectorProperties {

    // +---------+---------------------------------+
    // | default | use WM URL, start WM, stub mode |
    // | test    | use WM URL, start WM, stub mode |
    // | imdb    | use own URL,no WM               |
    // | record  | use WM URL ,no WM               |
    // +---------+---------------------------------+

    private String imdbUrl;

    private String wiremockPort;

    private Boolean startWiremockServer;

    public String getImdbUrl() {
        return imdbUrl;
    }

    public void setImdbUrl(String imdbUrl) {
        this.imdbUrl = imdbUrl;
    }

    public String getWiremockPort() {
        return wiremockPort;
    }

    public void setWiremockPort(String wiremockPort) {
        this.wiremockPort = wiremockPort;
    }

    public Boolean getStartWiremockServer() {
        return startWiremockServer;
    }

    public void setStartWiremockServer(Boolean startWiremockServer) {
        this.startWiremockServer = startWiremockServer;
    }

    public String determineUrlToCall(String movieUrl) {
        if (wiremockPort == null || wiremockPort.equals("")) {
            return movieUrl;
        }
        try {
            URL url = new URL(movieUrl);
            return "http://localhost:" + wiremockPort + url.getPath();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
```

3. Add ```nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer```:

```java
public interface ProxyServer {}
```

4. Add ```nl._42.qualityws.cleancode.shared.test.imdb.WiremockStubServer```:

```java
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tomakehurst.wiremock.WireMockServer;

public class WiremockStubServer implements ProxyServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WiremockStubServer.class);

    private WireMockServer wireMockServer;

    private final Integer wiremockPort;

    public WiremockStubServer(String wiremockPort) {
        this.wiremockPort = Integer.parseInt(wiremockPort);
    }

    @PostConstruct
    public void startWiremock() {
        LOGGER.info("Starting WireMock server");
        wireMockServer = new WireMockServer(options().port(wiremockPort));
        wireMockServer.start();
    }

    @PreDestroy
    public void stopWiremock() {
        LOGGER.info("Ending WireMock server");
        wireMockServer.stop();
    }

}
```

5. Add ```nl._42.qualityws.cleancode.shared.test.imdb.WiremockConfiguration```:

```java
import nl._42.qualityws.cleancode.collectors_item.service.imdb.ProxyServer;
import nl._42.qualityws.cleancode.config.CollectorProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CollectorProperties.class)
public class WiremockConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(WiremockConfiguration.class);

    private final CollectorProperties properties;

    public WiremockConfiguration(CollectorProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(name="collector.imdb.startWiremockServer")
    public ProxyServer imdbMockServer() {
        LOGGER.info("Creating WiremockStubServer on port {}", properties.getWiremockPort());
        return new WiremockStubServer(properties.getWiremockPort());
    }

}
```
6. Modify ```src/test/resources/application.yml```:

```yaml
collector:
  imdb:
    wiremock-port: 8089
    imdb-url: http://www.imdb.com
    start-wiremock-server: true
```

7. Add ```src/test/resources/application-imdb.yml```:

```yaml
collector:
  imdb:
    wiremock-port:
    start-wiremock-server: false
```

8. Add ```src/test/resources/application-record.yml```:

```yaml
collector:
  imdb:
    start-wiremock-server: false
```

9. Update ```nl._42.qualityws.cleancode.collectors_item.service.imdb.ImdbClient```:

```java
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.PostProcessor;
import nl._42.qualityws.cleancode.config.CollectorProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@EnableConfigurationProperties(CollectorProperties.class)
public class ImdbClient implements PostProcessor {

    protected final static Logger LOGGER = LoggerFactory.getLogger(ImdbClient.class);

    public static final String RATING_START = "<span itemprop=\"ratingValue\">";
    public static final String RATING_END = "</span>";

    private final CollectorProperties properties;

    @Autowired(required = false)
    private ProxyServer proxyServer;

    public ImdbClient(CollectorProperties properties) {
        this.properties = properties;
        LOGGER.info("Collector properties:");
        LOGGER.info("- imdb-url:              {}", properties.getImdbUrl());
        LOGGER.info("- wiremock-port:         {}", properties.getWiremockPort());
        LOGGER.info("- start-wiremock-server: {}", properties.getStartWiremockServer());
    }

    @Override
    public void process(CollectorsItem item) {
        if (!matches(item.getClass())) {
            return;
        }
        Movie movie = (Movie)item;
        movie.setImdbRating(parseRating(movie.getImdbUrl()));
        LOGGER.info("Rating for {} is {}", movie.getName(), movie.getImdbRating());
    }

    public boolean matches(Class<? extends CollectorsItem> itemClass) {
        return itemClass.equals(Movie.class);
    }

    private String parseRating(String imdbUrl) {
        String url = properties.determineUrlToCall(imdbUrl);
        LOGGER.info("Original URL {}, used URL {}", imdbUrl, url);
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            HttpEntity entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            String response = responseEntity.getBody();
            int ratingStart = response.indexOf(RATING_START);
            int ratingEnd = response.indexOf(RATING_END, ratingStart);
            if (ratingStart == -1 || ratingEnd == -1) {
                LOGGER.error("Unable to find rating for {}", url);
                return "-";
            }
            return response.substring(ratingStart + RATING_START.length(), ratingEnd);
        } catch (Exception err) {
            LOGGER.error("Unable to find rating for {}", url);
            return "-";
        }
    }

}
```

10. Download the Wiremock Standalone JAR: http://repo1.maven.org/maven2/com/github/tomakehurst/wiremock-standalone/2.6.0/wiremock-standalone-2.6.0.jar

Save the JAR in folder ```src/test/resources```

11. Use the IMDB run method. Verify that the calls are made directly to IMDB:

```
mvn spring-boot:run -Dspring.profiles.active=imdb
```

12. Start a local Wiremock to record all transactions to Wiremock:

```
cd src/test/resources
java -jar wiremock-standalone-2.6.0.jar --proxy-all="http://www.imdb.com" --record-mappings --verbose --port 8089
```

Start the application in the Record run mode:
 
```
mvn spring-boot:run -Dspring.profiles.active=imdb
```

Two folders will now be created:
```
__files
mappings
```

13. Use the Instant Developer Experience:

```
mvn spring-boot:run
```

And the unit tests:

```
mvn spring-boot:run
```

Both run modes make use of the Wiremock stubs.
