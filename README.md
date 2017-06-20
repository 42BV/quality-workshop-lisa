# Milestone 03
Add a new feature that scrapes the movie ratings from IMDB and stores those in the database. 

## System requirements
Make sure the following tools are installed on the system:
* Maven v3+
* Java 8+
* Docker

## Steps

1. Add IMDB rating to ```Movie```, including getters/setters:

```java
private String imdbRating;
```

2. Add IMDB rating to ```MovieResult```:

```java
public String imdbRating;
```

3. Add IMDB rating to table ```collectors_item``` via Liquibase file ```changeset_000.xml```:

```xml
<column name="imdb_rating" type="varchar(5)"/>
```

4. add ```nl._42.qualityws.cleancode.collectors_item.service.PostProcessor```: 

```java
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;

public interface PostProcessor {
    void process(CollectorsItem item);
}
```

5. add ```nl._42.qualityws.cleancode.collectors_item.service.imdb.ImdbClient```:

```java
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.PostProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImdbClient implements PostProcessor {

    protected final static Logger LOGGER = LoggerFactory.getLogger(ImdbClient.class);

    public static final String RATING_START = "<span itemprop=\"ratingValue\">";
    public static final String RATING_END = "</span>";

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
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            HttpEntity entity = new HttpEntity<>(httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(imdbUrl, HttpMethod.GET, entity, String.class);
            String response = responseEntity.getBody();
            int ratingStart = response.indexOf(RATING_START);
            int ratingEnd = response.indexOf(RATING_END, ratingStart);
            if (ratingStart == -1 || ratingEnd == -1) {
                LOGGER.error("Unable to find rating for {}", imdbUrl);
                return "-";
            }
            return response.substring(ratingStart + RATING_START.length(), ratingEnd);
        } catch (Exception err) {
            LOGGER.error("Unable to find rating for {}", imdbUrl);
            return "-";
        }
    }

}
```

6. Modify ```nl._42.qualityws.cleancode.collectors_item.service.CollectorsItemService```:

```java
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.beanmapper.BeanMapper;
import nl._42.qualityws.cleancode.collectors_item.Album;
import nl._42.qualityws.cleancode.collectors_item.Book;
import nl._42.qualityws.cleancode.collectors_item.CollectorsItem;
import nl._42.qualityws.cleancode.collectors_item.Movie;
import nl._42.qualityws.cleancode.collectors_item.service.csv.CollectorsItemCsvReaderFacade;
import nl._42.qualityws.cleancode.collectors_item.service.imdb.ImdbClient;

@Service
public class CollectorsItemService {

    protected final static Logger LOGGER = LoggerFactory.getLogger(CollectorsItemService.class);

    @Autowired
    private CollectorsItemRepository collectorsItemRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CollectorsItemCsvReaderFacade csvReader;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private MovieValidator movieValidator;

    @Autowired
    private AlbumValidator albumValidator;

    @Autowired
    private BookValidator bookValidator;

    @Autowired
    private ImdbClient imdbClient;

    private List<PostProcessor> postProcessors = new ArrayList<>();

    @PostConstruct
    public void setupPostProcessors() {
        postProcessors.add(imdbClient);
    }

    public <T extends CollectorsItem> T create(T item) {
        notNull(item, "Collectors' item to create may not be null");
        isTrue(item.isNew(), "Cannot create existing collectors' item");
        return save(item);
    }

    private <T extends CollectorsItem> T createOrUpdate(T item) {
        if (item.isNew()) {
            return create(item);
        } else {
            return save(item);
        }
    }

    private <T extends CollectorsItem> T save(T item) {
        item = postProcessItem(item);
        return collectorsItemRepository.save(item);
    }

    private <T extends CollectorsItem> T postProcessItem(T item) {
        postProcessors.forEach(processor -> processor.process(item));
        return item;
    }

    public void importBooks(InputStream bookStream) {
        Collection<Book> books = csvReader.readBooks(bookStream);
        merge(bookValidator, books);
    }

    public void importMovies(InputStream movieStream) {
        Collection<Movie> movies = csvReader.readMovies(movieStream);
        merge(movieValidator, movies);
    }

    public void importAlbums(InputStream albumStream) {
        Collection<Album> albums = csvReader.readAlbums(albumStream);
        merge(albumValidator, albums);
    }

    private <T extends CollectorsItem> void merge(CollectorsItemValidator<T> validator, Collection<T> items) {
        for (T item : items) {
            if (!validator.validate(item)) {
                continue;
            }
            createOrUpdate(mergeItem(item));
        }
    }

    private CollectorsItem mergeItem(CollectorsItem item) {
        CollectorsItem foundItem = collectorsItemRepository.findByName(item.getName());
        if (foundItem == null) {
            return item;
        }
        LOGGER.info("Item already exists [{}], merging", item.getName());
        return beanMapper.map(item, foundItem);
    }

    public Page<Movie> listMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Page<Album> listAlbums(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Page<Book> listBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

}
```

7. Add assertions to ```CollectorsItemControllerTest.uploadMovies_shouldSucceed_whenMoviesCsvIsProvided```:

```java
Movie movie = movies.getContent().get(0);
assertEquals("The Shawshank Redemption", movie.getName());
assertEquals("9.3", movie.getImdbRating());
```

8. Run the application and verify that rating are fetched. Verify with pgadmin that ratings are stored.

```
mvn spring-boot:run
```

9. Run your unit tests. Note that one tests takes a long time. This one goes directly to IMDB.

10. Turn off your wifi. Run the unit tests. Note that the slow test now fails.