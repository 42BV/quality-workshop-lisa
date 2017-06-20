package nl._42.qualityws.cleancode.collectors_item.service.imdb;

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